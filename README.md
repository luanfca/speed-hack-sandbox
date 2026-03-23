# SpeedHack-Sandbox (sem root)

Hook de tempo para Android via sandbox / clone.
Nao requer root; funciona dentro de VirtualXposed, Parallel Space, etc. O hook age em memoria usando `xhook`, modificando as chamadas `clock_gettime` e `gettimeofday`.

## Estrutura

```text
speed-hack-sandbox/
|- app/
|  |- src/main/
|  |  |- cpp/
|  |  |- java/io/github/seuuser/speedhksb/
|  |  |- res/layout/
|  |  `- AndroidManifest.xml
|  |- build.gradle
|  `- proguard-rules.pro
|- .github/workflows/build.yml
|- build.gradle
|- gradle.properties
|- settings.gradle
`- README.md
```

## Requisitos de Build

- Android Studio 2023+
- Android NDK 26+
- CMake 3.26+
- Gradle Wrapper configurado no repositorio

## Como clonar e compilar

Como o projeto usa o `xhook` via git submodule, clone usando a flag recursiva:

```bash
git clone --recursive https://github.com/seuuser/speed-hack-sandbox.git
cd speed-hack-sandbox
./gradlew assembleRelease
```

## Observacoes

- O diretorio `app/src/main/cpp/xhook` precisa existir como submodule antes do build nativo.
- O workflow do GitHub Actions assume a presenca do `gradlew` e dos arquivos de wrapper.
