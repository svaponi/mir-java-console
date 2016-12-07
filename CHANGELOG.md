# mir-java-console

### 1.3.0
Definito `RuntimeUtil` per utilizzare facilmente le funzionalità di `RuntimeClassCompiler` e `RuntimeObjectFactory`.
Cambiata la struttura dei package, spostato tutto sotto `it.miriade.runtime`.
Importato il codice sorgente di [InMemoryJavaCompiler](https://github.com/trung/InMemoryJavaCompiler).


### 1.2.0
In `JavaConsole` aggiunti:
* comandi speciali con prefisso `:` (semi-colon), es. `:exit` per uscire dalla console.
* esecuzione immediata del comando con suffisso `!` (esclamation mark)


### 1.1.0
Distinzione tra package `it.miriade.console` (contiene la Console Java) e package `it.miriade.runtime` (contiene gli elementi necessari alla generazione di oggetti a runtime).
Creazione del `it.miriade.runtime.JavaResultBuilder` con il solo scopo di generare oggetti dalla esecuzione di porzione di codice compilato ed eseguito a runtime. La diferenza principale dalla `JavaConsole` è che quest'ultima esegue del codice senza tornare nessun oggetto.
Introduzione della `it.miriade.runtime.core.RuntimeClassDefinition` per incapsulare il codice fintanto che non viene compilato. 

### 1.0.0
Implementazione di `it.miriade.console.JavaConsole`, `RuntimeClassCompiler` e `RuntimeObjectFactory`.
