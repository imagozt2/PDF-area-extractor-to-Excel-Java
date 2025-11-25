# PDF Area Extractor to Excel (Java)

[![Vídeo](https://i.postimg.cc/bYkfwfMH/Diseno-sin-titulo.png)](https://www.youtube.com/watch?v=R5lbnZJC59U)

"PDF Area Extractor to Excel (Java)" es un programa que he creado con el objetivo de automatizar tareas administrativas. Este programa sirve para extraer información de documentos de texto digital (no fotocopias) donde la información se estructura en forma de tablas o siguiendo un formato estandarizado por cada página. Es decir, solo servirá para documentos en los que la información se estructure de la misma manera en cada página, como facturas, formularios, encuestas, documentos bancarios y otros.

Este programa permite al usuario definir una serie de campos en una lista, que darán origen a las columnas del Excel generado. A cada campo de la lista se le puede asignar un área del PDF, y el programa recorrerá el documento guardando en forma de filas los datos ordenados según los campos definidos por el usuario en la lista de campos, resultando de una tabla de Excel. Además, el programa permite crear campos con valores incrementales, cosa que facilita la gestión del documento en tareas como el análisis de datos o la importación a bases de datos.

---

## Componentes de la interfaz

A continuación se muestra en imágenes la interfaz con sus componentes enumerados, seguidas de tablas que ofrecen algunos detalles como su nombre, el tipo de componente y su función.

<p align="center">
  <img src="https://i.postimg.cc/NjkBdvYG/1.png" alt="Interfaz parte 1" width="100%">
</p>

| Nº | Nombre del componente | Tipo de componente | Función |
|----|------------------------|--------------------|---------|
| 1  | `lblLoadFile` | JLabel | Informativo. |
| 2  | `btnSelector` | JButton | Abre el selector de archivos para cargar un PDF. |
| 3  | `txfLink` | JTextField | Muestra la ruta del archivo PDF cargado. |
| 4  | `lblStructure` | JLabel | Informativo. |
| 5  | `imgPage1` | JLabel (icono) | Complemento gráfico. |
| 6  | `imgPage2` | JLabel (icono) | Complemento gráfico. |
| 7  | `imgPage3` | JLabel (icono) | Complemento gráfico. |
| 8  | `imgPage4` | JLabel (icono) | Complemento gráfico. |
| 9  | `imgPage5` | JLabel (icono) | Complemento gráfico. |
| 10 | `rdbStructure1` | JRadioButton | Define el recorrido cada dos caras. |
| 11 | `rdbStructure2` | JRadioButton | Define el recorrido en cada cara para los mismos datos. |
| 12 | `rdbStructure3` | JRadioButton | Define el recorrido en cada cara para datos distintos. |
| 13 | `lblPagesScanner` | JLabel | Informativo. |
| 14 | `rdbPagesScanner1` | JRadioButton | Define el recorrido de todas las páginas. |
| 15 | `rdbPagesScanner2` | JRadioButton | Define el recorrido de un rango de páginas. |
| 16 | `lblPageStart` | JLabel | Informativo. |
| 17 | `txfStart` | JTextField | Guarda el número de la página de inicio del recorrido. |
| 18 | `lblPageEnd` | JLabel | Informativo. |
| 19 | `txfEnd` | JTextField | Guarda el número de la página de inicio del recorrido. |

<p align="center">
  <img src="https://i.postimg.cc/y8ysLCB8/20.png.png" alt="Interfaz parte 2" width="100%">
</p>

| Nº | Nombre del componente | Tipo de componente | Función |
|----|------------------------|--------------------|---------|
| 20 | `lblData` | JLabel | Informativo. |
| 21 | `lblDataList` | JLabel | Informativo. |
| 22 | `lstDataList` | JList | Lista de campos definidos por el usuario. |
| 23 | `btnAddField` | JButton | Añade un nuevo campo a la lista. |
| 24 | `btnDeleteField` | JButton | Elimina el campo seleccionado. |
| 25 | `btnClearList` | JButton | Borra toda la lista de campos. |
| 26 | `btnEditField` | JButton | Edita el nombre del campo seleccionado. |
| 27 | `btnMoveUpField` | JButton | Mueve el campo seleccionado hacia arriba. |
| 28 | `btnMoveDownField` | JButton | Mueve el campo seleccionado hacia abajo. |
| 29 | `lblDataFieldType` | JLabel | Informativo. |
| 30 | `rdbFieldTypeUnique` | JRadioButton | Define un campo como valor único. |
| 31 | `rdbFieldTypeMaster` | JRadioButton | Define un campo como valor múltiple maestro. |
| 32 | `rdbFieldTypeDependent` | JRadioButton | Define un campo como valor múltiple dependiente. |
| 33 | `lblDataUbication` | JLabel | Informativo. |
| 34 | `lblPage` | JLabel | Informativo. |
| 35 | `txfPage` | JTextField | Muestra en qué cara de la página se encuentra el área seleccionada. |
| 36 | `lblAxisX` | JLabel | Informativo. |
| 37 | `txfAxisX` | JTextField | Muestra la coordenada X del área seleccionada. |
| 38 | `lblAxisY` | JLabel | Informativo. |
| 39 | `txfAxisY` | JTextField | Muestra la coordenada Y del área seleccionada. |
| 40 | `lblDataOptions` | JLabel | Informativo. |
| 41 | `lblMaster` | JLabel | Informativo. |
| 42 | `cmbMaster` | JComboBox | Permite seleccionar el campo maestro (si el campo es dependiente). |
| 43 | `chkItem` | JCheckBox | Activa el modo "campo sin área". |
| 44 | `cmbItem` | JComboBox | Define el tipo de dato del campo sin área (ID, PAGE, DATE). |
| 45 | `chkText` | JCheckBox | Activa la normalización de texto. |
| 46 | `cmbText` | JComboBox | Selecciona el tipo de normalización (UPPERCASE, lowercase, Title). |
| 47 | `chkSpaces` | JCheckBox | Elimina espacios dentro del texto extraído. |
| 48 | `chkSymbols` | JCheckBox | Elimina caracteres no numéricos del texto. |
| 49 | `btnValidate` | JButton | Valida la configuración de los campos y recorre el documento para guardar los datos. |
| 50 | `btnGenerate` | JButton | Genera el archivo Excel con los datos extraídos. |

<p align="center">
  <img src="https://i.postimg.cc/Sxfkgbq4/51.png" alt="Interfaz parte 3" width="100%">
</p>

| Nº | Nombre del componente | Tipo de componente | Función |
|----|------------------------|--------------------|---------|
| 51 | `tglPag1` | JToggleButton | Selecciona la cara delantera del documento. |
| 52 | `tglPag2` | JToggleButton | Selecciona la cara trasera del documento. |
| 53 | `sldRightPanel` | JSlider | Controla el nivel de zoom del visor PDF. |

---

## Estructura del proyecto y clases

El proyecto está organizado en varios paquetes, separando la parte gráfica, la lógica de control y los modelos de datos.

### Paquete `pdfareaextractortoexcel.app`

- **`PDFAreaExtractorApp`**  
  Es la clase principal de la aplicación. Se encarga de crear la ventana (JFrame), inicializar todos los componentes de la interfaz y exponer getters para que el controlador (`MainController`) pueda manejarlos. Es el punto de entrada del programa (`main`).

### Paquete `pdfareaextractortoexcel.view`

- **`PDFPagePanel`**  
  Es un panel personalizado que se encarga de mostrar las páginas del PDF como imagen, gestionar el zoom, el scroll y la selección de áreas con el ratón. También dibuja visualmente las áreas ya definidas sobre el documento.

### Paquete `pdfareaextractortoexcel.services`

- **`MainController`**  
  Es el controlador principal de la aplicación. Conecta la interfaz con la lógica interna: gestiona los eventos de los botones y campos, coordina la carga del PDF, la definición de campos, la selección de áreas, la validación de la configuración y la llamada al motor de extracción y exportación.

- **`FieldManager`**  
  Gestiona la lista de campos definidos por el usuario. Se encarga de crear, editar, eliminar y reordenar los campos, manteniendo sincronizada la lista de campos (`lstDataList`) con los objetos de configuración (`FieldConfig`).

- **`AreaSelectionManager`**  
  Gestiona las áreas asociadas a cada campo. Convierte las selecciones realizadas en el `PDFPagePanel` a coordenadas relativas, las guarda en los campos correspondientes y se encarga de actualizar la vista de las áreas en el PDF.

- **`ExtractionEngine`**  
  Es el núcleo de la extracción de datos. Recorre el documento PDF según la estructura y el rango configurados, utiliza las áreas de cada campo para extraer texto y construye las filas que posteriormente se volcarán al Excel.

- **`ExcelExporter`**  
  Es l encargado de generar el archivo Excel final a partir de los datos extraídos. Crea las columnas a partir de los campos definidos y vuelca las filas de resultados utilizando Apache POI.

- **`PDFTextProcessor`**  
  Se encarga de dar apoyo al motor de extracción, realizando operaciones sobre el texto extraído: normalización (mayúsculas, minúsculas, título), eliminación de espacios o símbolos, conversión de coordenadas de áreas a rectángulos PDFBox, etc.

### Paquete `pdfareaextractortoexcel.model`

- **`FieldConfig`**  
  Modelo que representa la configuración de un campo de la lista. Almacena el nombre del campo, su tipo (único, maestro, dependiente), las áreas por página, si es un campo sin área, el tipo de dato asociado, opciones de normalización y limpieza, y la relación con un campo maestro si aplica.

- **`ExtractionSettings`**  
  Contiene los ajustes globales de extracción: modo de estructura del documento (cada página, cada dos páginas, caras distintas), modo de escaneo (documento completo o rango) y páginas de inicio y fin.

- **Enums (`FieldType`, `ItemType`, `PageScanMode`, `StructureMode`, `TextCaseType`)**  
  Conjuntos de valores predefinidos que describen:
  - El tipo de campo (`FieldType`)
  - El tipo de campo sin área (por ejemplo, ID, fecha, página) (`ItemType`)
  - El modo de escaneo de páginas (`PageScanMode`)
  - La estructura del documento (cada página, cada dos, caras distintas) (`StructureMode`)
  - El tipo de normalización de texto (`TextCaseType`)

---

## Entorno de desarrollo y librerías

Este proyecto ha sido desarrollado utilizando un entorno Java estándar basado en NetBeans y librerías externas para el tratamiento de PDF y la generación de archivos Excel. A continuación se detallan las herramientas y dependencias utilizadas.

### Entorno de desarrollo
- **Sistema operativo:** Windows 11  
- **IDE:** Apache NetBeans IDE 25  
- **JDK utilizado:** JDK 24

El proyecto está estructurado como un proyecto clásico de NetBeans, con carpetas `src/`, `nbproject/`, `graphicresources/`, etc.

### Librerías utilizadas

A continuación se listan todas las librerías externas añadidas al proyecto. Cada una incluye una breve descripción de su función dentro del programa.

#### Apache Commons

- **commons-collections4-4.4.jar**  
  Proporciona colecciones y estructuras de datos avanzadas utilizadas por algunas funciones internas de Apache POI.

- **commons-compress-1.27.1.jar**  
  Gestión de compresión ZIP. Necesario para trabajar con archivos XLSX, ya que estos son contenedores ZIP.

#### Log4j (requerido por dependencias internas)

- **log4j-api-2.17.1.jar**  
  API de logging utilizada por POI y otros componentes que generan trazas de ejecución.

- **log4j-core-2.17.1.jar**  
  Implementación principal de Log4j. Se usa para registrar mensajes internos y soporte de depuración.

#### Apache PDFBox (procesamiento de PDF)

- **pdfbox-app-3.0.5.jar**  
  Librería principal para trabajar con archivos PDF.  
  Permite:
  - Cargar el PDF
  - Extraer texto por áreas (`PDFTextStripperByArea`)
  - Convertir páginas en imágenes para mostrarlas en `PDFPagePanel`
  - Manipular coordenadas y estructuras internas del PDF

#### Apache POI (generación de Excel)

- **poi-5.2.3.jar**  
  Núcleo de Apache POI para manipulación de documentos Microsoft.

- **poi-ooxml-5.2.3.jar**  
  Soporte para archivos Excel en formato OOXML (.xlsx).  
  Permite crear libros, hojas, filas y celdas.

- **poi-ooxml-full-5.2.3.jar**  
  Conjunto extendido de esquemas OOXML.  
  Necesario para asegurar compatibilidad completa con Excel moderno.

- **xmlbeans-5.2.0.jar**  
  Motor de procesamiento XML utilizado por POI  
  (obligatorio para interpretar la estructura interna de los archivos XLSX).

### Instalación del proyecto y generación de un ejecutable

Para compilar, ejecutar o generar un ejecutable, basta con:
- Disponer de una IDE ("Apache NetBeans IDE 25" preferiblemente)
- Instalar un JDK ("JDK 24" preferiblemente)
- Añadir las mismas librerías en *Project → Properties → Libraries*

Tras hacer un "Buidl and Clean", el ejecutable se generaría dentro de la carpeta "dist" del proyecto junto a una carpeta con las librerías. No debe separarse el ejecutable de la carpeta que contiene las librerías. En caso de querer tener el ejecutable en otro lugar (otra carpeta o en el escritorio) conviene copiar el ejecutable y pegarlo en forma de acceso directo donde queramos tenerlo.

---

## Licencia

Este proyecto está protegido bajo la licencia **[Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)](https://creativecommons.org/licenses/by-nc/4.0/)**.


Esto significa que:

- Puedes usar el código libremente.
- Puedes modificarlo y adaptarlo.
- Puedes compartirlo con otras personas.
- **No puedes utilizarlo con fines comerciales.**
- **No puedes vender el código ni integrarlo en productos o servicios de pago.**
- Debes atribuir al autor original en cualquier redistribución.

### Permisos especiales
Si deseas un permiso específico para uso comercial, puedes solicitarlo contactando directamente conmigo.

