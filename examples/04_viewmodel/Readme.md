## ViewModel

In diesem Beispiel soll die Logik eines einfachen Quiz als ViewModel implementiert werden. 

Das Fragment mit dem Layout für das Quiz ist bereits vorhanden ("GameFragment") und soll mit der ViewModel-Implementierung verbunden werden. 

### Task 1: NavigationGraph
Als Wiederholung soll zunächst die "MainActivity" als Host für unsere Single-Page-App eingerichtet werden und ein zugehöriger NavigationGraph erstellt werden. 

* NavigationGraph Ressource anlegen
* NavHostFragment in MainActivity verwenden
* Als Navigationsstart soll ein neues ``StartFragment`` verwendet werden. Dort soll ein Button (TextView) mit dem Label "Start Quiz" angezeigt werden. Bei Klick auf den Button soll auf das ``GameFragment`` navigiert werden. 
* Für den Zugriff auf die Elemente im Layout soll ``viewBinding`` verwendet werden. 

### Task 2: GameViewModel

Es soll Schritt für Schritt die Logik des Quiz-Spiels für das ``GameFragment`` aufgebaut und verwendet werden. 

* Die Fragen werden noch nicht via REST geladen, sondern nur hardcoded erzeugt. Um die zukünftige Struktur gleich zu berücksichtigen, soll eine ``Question`` Klasse verwendet werden, die dem JSON einer Frage entspricht, wie sie vom zu künftigen REST API geschickt wird ([Open Trivia Database](https://opentdb.com)).
* Beginnen Sie mit einem einfachen ``GameViewModel`` und der Darstellung einer einzelnen Frage im ``GameFragment``.
* Die Antworten sollten zufällig verteilt auf den 4 Antwort-Buttons angezeigt werden.
* Bei Klick auf einen der Antwort-Buttons soll die Frage intern "beantwortet" werden und die Button-Hintergründe entsprechend der Richtigkeit der Antwort angepasst werden.
* Die Frage sollte nur einmal beantwortet werden können.
* Erweitern Sie dann das ``GameViewModel`` auf 10 Fragen.
* Wenn eine Frage beantwortet ist, soll durch Klick auf den ``Continue``-Button die nächste Frage angezeigt werden.
* Bei der Beantwortung einer Frage soll ein Timer gestartet werden und die verbleibende Ratezeit über den ProgresssBar im ``GameFragment`` angezeigt werden. Nach Ablauf der Ratezeit soll die Frage als falsch beantwortet bewertet werden, falls sie nicht durch den Benutzer beantwortet wurde.
