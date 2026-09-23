# Implementación de Splash Screen y Pantalla de Inicio de Sesión (Mercado Libre Clone)

Este plan detalla la creación de la pantalla de bienvenida (Splash) y la pantalla de inicio de sesión basándose en el logo y el diseño proporcionados.

## User Review Required

> [!IMPORTANT]
> **Recurso del Logo:** Para que la aplicación funcione correctamente, debes guardar tu logo en la carpeta `app/src/main/res/drawable/` con el nombre `logo_ml.png` (o el formato que tengas). En el código usaré `R.drawable.logo_ml`.

## Proposed Changes

### [Component Name] UI & Theme

#### [MODIFY] [Color.kt](file:///C:/APP_2026_2/app_mcl/app/src/main/java/com/example/app_clon_mercado_libre/ui/theme/Color.kt)
*   Agregar colores corporativos de Mercado Libre: Amarillo (#FFF159) y Azul (#3483FA).

#### [MODIFY] [libs.versions.toml](file:///C:/APP_2026_2/app_mcl/gradle/libs.versions.toml)
*   Agregar la dependencia de `navigation-compose` para el manejo de pantallas.

#### [MODIFY] [build.gradle.kts](file:///C:/APP_2026_2/app_mcl/app/build.gradle.kts)
*   Aplicar la dependencia de navegación.

### [Component Name] Screens

#### [NEW] [SplashScreen.kt](file:///C:/APP_2026_2/app_mcl/app/src/main/java/com/example/app_clon_mercado_libre/ui/screens/SplashScreen.kt)
*   Pantalla con fondo amarillo y el logo centrado que navega al Login tras 2 segundos.

#### [NEW] [LoginScreen.kt](file:///C:/APP_2026_2/app_mcl/app/src/main/java/com/example/app_clon_mercado_libre/ui/screens/LoginScreen.kt)
*   Header amarillo con logo.
*   Campo de texto para e-mail/usuario.
*   Botones de "Continuar", "Crear cuenta", "Ingresar con Google" y "Ayuda".

#### [MODIFY] [MainActivity.kt](file:///C:/APP_2026_2/app_mcl/app/src/main/java/com/example/app_clon_mercado_libre/MainActivity.kt)
*   Configurar el `NavHost` para manejar el flujo entre Splash y Login.

## Verification Plan

### Manual Verification
1.  **Ejecutar la App:** Verificar que aparezca el Splash Screen con fondo amarillo.
2.  **Navegación:** Comprobar que después de unos segundos cambie a la pantalla de Login.
3.  **Diseño de Login:** Validar que el header sea amarillo, el botón "Continuar" sea azul y los elementos estén alineados según la imagen proporcionada.
