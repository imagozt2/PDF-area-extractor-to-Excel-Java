# PDF Area Extractor to Excel (Java)

<a href="https://www.youtube.com/watch?v=R5lbnZJC59U" target="_blank">
    <img src="https://img.youtube.com/vi/R5lbnZJC59U/maxresdefault.jpg" 
         alt="Vídeo demostración" width="100%">
</a>

## Componentes de la interfaz

![Interfaz - Parte 1](https://i.postimg.cc/bZr7vMs4/1.png)
![Interfaz - Parte 2](https://i.postimg.cc/yDxMN5kM/20.png)
![Interfaz - Parte 3](https://i.postimg.cc/DS09zD8p/51.png)

| Nº | Componente en la UI | Variable en código | Descripción |
|----|----------------------|--------------------|-------------|
| 1  | Cargar archivo       | `lblLoadFile`        | Título de la sección de carga |
| 2  | Botón “Buscar archivo” | `btnSelector`      | Abre el selector de archivos PDF |
| 3  | Ruta del archivo     | `txfLink`           | Muestra la ruta del PDF cargado |
| 4  | Estructura del documento | `lblStructure` | Título de la sección de estructura |
| 5  | Icono estructura 1 (cara 1) | `imgPage1` | Imagen representativa del modo 1 |
| 6  | Icono estructura 1 (cara 2) | `imgPage2` | Imagen complementaria del modo 1 |
| 7  | Icono estructura 2         | `imgPage3` | Imagen representativa del modo 2 |
| 8  | Icono estructura 3 (cara delantera) | `imgPage4` | Imagen modo 3 cara delantera |
| 9  | Icono estructura 3 (cara trasera) | `imgPage5` | Imagen modo 3 cara trasera |
| 10 | Todos los datos cada dos páginas | `rdbStructure1` | RadioButton para estructura 1 |
| 11 | Todos los datos en cada página | `rdbStructure2` | RadioButton para estructura 2 |
| 12 | Datos distintos por las dos caras | `rdbStructure3` | RadioButton para estructura 3 |
| 13 | Páginas a escanear | `lblPagesScanner` | Título sección rango |
| 14 | Documento completo | `rdbPagesScanner1` | Escaneo completo |
| 15 | Margen personalizado | `rdbPagesScanner2` | Escaneo por rango |
| 16 | Iniciar en página | `lblPageStart` | Etiqueta inicio |
| 17 | Valor inicio página | `txfStart` | Campo inicio |
| 18 | Finalizar en página | `lblPageEnd` | Etiqueta fin |
| 19 | Valor fin página | `txfEnd` | Campo fin |
| 20 | Campos y recopilación de datos | `lblData` | Título sección de campos |
| 21 | Lista de campos (título) | `lblDataList` | Etiqueta sobre la lista |
| 22 | Lista de campos | `lstDataList` | Contenedor donde se muestran los campos |
| 23 | Añadir campo | `btnAddField` | Añade un nuevo campo |
| 24 | Eliminar campo | `btnDeleteField` | Borra el campo seleccionado |
| 25 | Borrar lista | `btnClearList` | Elimina todos los campos |
| 26 | Editar campo | `btnEditField` | Modificar configuración del campo |
| 27 | Mover arriba | `btnMoveUpField` | Orden de la lista |
| 28 | Mover abajo | `btnMoveDownField` | Orden descendente |
| 29 | Tipo de campo | `lblDataFieldType` | Título de sección |
| 30 | Valor único | `rdbFieldTypeUnique` | Campo simple |
| 31 | Valor múltiple maestro | `rdbFieldTypeMaster` | Campo que genera varias filas |
| 32 | Valor múltiple dependiente | `rdbFieldTypeDependent` | Depende de un maestro |
| 33 | Ubicación y coordenadas | `lblDataUbication` | Título |
| 34 | Cara | `lblPage` | Etiqueta de cara |
| 35 | Valor cara | `txfPage` | Cara de asignación |
| 36 | Eje X | `lblAxisX` | Coordenada X |
| 37 | Valor X | `txfAxisX` | Coordenada X relativa |
| 38 | Eje Y | `lblAxisY` | Coordenada Y |
| 39 | Valor Y | `txfAxisY` | Coordenada Y relativa |
| 40 | Otras opciones | `lblDataOptions` | Grupo de opciones |
| 41 | Respectivo campo maestro | `lblMaster` | Etiqueta combo maestro |
| 42 | Combo maestro | `cmbMaster` | Selección del maestro |
| 43 | Definir campo sin área | `chkItem` | Campos fijos |
| 44 | Tipo sin área | `cmbItem` | ID / PAGE / DATE |
| 45 | Normalizar texto | `chkText` | Activación normalización |
| 46 | Tipo de normalización | `cmbText` | UPPER / lower / Title |
| 47 | Eliminar espacios | `chkSpaces` | Limpieza |
| 48 | Eliminar caracteres no numéricos | `chkSymbols` | Solo dígitos |
| 49 | Validar datos | `btnValidate` | Recorrido del documento y toma de datos |
| 50 | Generar Excel | `btnGenerate` | Exporta archivo final |
| 51 | Cara delantera | `tglPag1` | Alterna cara delantera |
| 52 | Cara trasera | `tglPag2` | Alterna cara trasera |
| 53 | Slider de zoom | `sldRightPanel` | Controla zoom del visor PDF |