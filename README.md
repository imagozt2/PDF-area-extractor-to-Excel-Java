# PDF Area Extractor to Excel (Java)

[![Vídeo](https://i.postimg.cc/bYkfwfMH/Diseno-sin-titulo.png)](https://www.youtube.com/watch?v=R5lbnZJC59U)

"PDF Area Extractor to Excel (Java)" es un programa que he creado con el objetivo de automatizar tareas administrativas. Este programa sirve para extraer información de documentos de texto digital (no fotocopias) donde la información se estructura en forma de tablas o siguiendo un formato estandarizado por cada página. Es decir, solo servirá para documentos en los que la información se estructure de la misma manera en cada página, como facturas, formularios, encuestas, documentos bancarios y otros.

Este programa permite al usuario definir una serie de campos en una lista, que darán origen a las columnas del Excel generado. A cada campo de la lista se le puede asignar un área del PDF, y el programa recorrerá el documento guardando en forma de filas los datos ordenados según los campos definidos por el usuario, resultando de una tabla de Excel. Además, el programa permite crear campos con valores incrementales, cosa que facilita la gestión del documento en tareas como el análisis de datos o la importación a bases de datos.


## Componentes de la interfaz

A continuación se muestra en imágenes la interfaz con sus componentes enumerados, seguidas de tablas que ofrecen algunos detalles como su nombre, el tipo de componente y su función.

<p align="center">
  <img src="https://i.postimg.cc/NjkBdvYG/1.png" alt="Interfaz parte 1" width="100%">
</p>

| Nº | Nombre del componente | Tipo de componente | Función |
|----|------------------------|--------------------|---------|
| 1  | `lblLoadFile` | Label | Informativo. |
| 2  | `btnSelector` | JButton | Abre el selector de archivos para cargar un PDF. |
| 3  | `txfLink` | JTextField | Muestra la ruta del archivo PDF cargado. |
| 4  | `lblStructure` | Label | Informativo. |
| 5  | `imgPage1` | JLabel (icono) | Complemento gráfico. |
| 6  | `imgPage2` | JLabel (icono) | Complemento gráfico. |
| 7  | `imgPage3` | JLabel (icono) | Complemento gráfico. |
| 8  | `imgPage4` | JLabel (icono) | Complemento gráfico. |
| 9  | `imgPage5` | JLabel (icono) | Complemento gráfico. |
| 10 | `rdbStructure1` | JRadioButton | Define el recorrido cada dos caras. |
| 11 | `rdbStructure2` | JRadioButton | Define el recorrido en cada cara para los mismos datos. |
| 12 | `rdbStructure3` | JRadioButton | Define el recorrido en cada cara para datos distintos. |
| 13 | `lblPagesScanner` | Label | Informativo. |
| 14 | `rdbPagesScanner1` | JRadioButton | Define el recorrido de todas las páginas. |
| 15 | `rdbPagesScanner2` | JRadioButton | Define el recorrido de un rango de páginas. |
| 16 | `lblPageStart` | Label | Informativo. |
| 17 | `txfStart` | JTextField | Guarda el número de la página de inicio del recorrido. |
| 18 | `lblPageEnd` | Label | Informativo. |
| 19 | `txfEnd` | JTextField | Guarda el número de la página de inicio del recorrido. |

<p align="center">
  <img src="https://i.postimg.cc/y8ysLCB8/20.png.png" alt="Interfaz parte 2" width="100%">
</p>

| Nº | Nombre del componente | Tipo de componente | Función |
|----|------------------------|--------------------|---------|
| 20 | `lblData` | Label | Informativo. |
| 21 | `lblDataList` | Label | Informativo. |
| 22 | `lstDataList` | JList | Lista de campos definidos por el usuario. |
| 23 | `btnAddField` | JButton | Añade un nuevo campo a la lista. |
| 24 | `btnDeleteField` | JButton | Elimina el campo seleccionado. |
| 25 | `btnClearList` | JButton | Borra toda la lista de campos. |
| 26 | `btnEditField` | JButton | Edita el nombre del campo seleccionado. |
| 27 | `btnMoveUpField` | JButton | Mueve el campo seleccionado hacia arriba. |
| 28 | `btnMoveDownField` | JButton | Mueve el campo seleccionado hacia abajo. |
| 29 | `lblDataFieldType` | Label | Informativo. |
| 30 | `rdbFieldTypeUnique` | JRadioButton | Define un campo como valor único. |
| 31 | `rdbFieldTypeMaster` | JRadioButton | Define un campo como valor múltiple maestro. |
| 32 | `rdbFieldTypeDependent` | JRadioButton | Define un campo como valor múltiple dependiente. |
| 33 | `lblDataUbication` | Label | Informativo. |
| 34 | `lblPage` | Label | Informativo. |
| 35 | `txfPage` | JTextField | Muestra en qué cara de la página se encuentra el área seleccionada. |
| 36 | `lblAxisX` | Label | Informativo. |
| 37 | `txfAxisX` | JTextField | Muestra la coordenada X del área seleccionada. |
| 38 | `lblAxisY` | Label | IInformativo. |
| 39 | `txfAxisY` | JTextField | Muestra la coordenada Y del área seleccionada. |
| 40 | `lblDataOptions` | Label | Informativo. |
| 41 | `lblMaster` | Label | Informativo. |
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
