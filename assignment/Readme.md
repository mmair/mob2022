## Aufgabenstellung

Erweitern Sie unsere Quiz-App um 

* die Möglichkeit, nach Abschluss eines Spiels durch Klick auf den entsprechenden Progress-Indikator im Header die entsprechende Frage darzustellen und
* einen Auswahl-Schirm für die Wahl einer Quiz-Kategorie (die Fragen sollen entsprechend der gewählten Kategorie geladen werden).


### Teil 1: Navigieren durch die Fragen nach Abschluss des Spiels

Im Header befinden sich die Progress-Indikatoren, welche anzeigen, ob die entsprechende Quiz-Frage beantwortet wurde (und ob die Antwort korrekt war oder nicht).

Wenn ein Spiel abgeschlossen ist (alle Fragen sind beantwortet), soll man durch Klick auf einen Progress-Indikator die entsprechende Frage angezeigt bekommen (inklusive der passenden Button-Backgrounds).

#### Tasks 

* GameViewModel:
	* Implementieren einer neuen Methode ``selectQuestion(index: Int)``
	* Nur, __falls alle Fragen bereits beantwortet wurden__, soll die Frage mit Index ``index`` ausgewählt werden und die angezeigten Texte (Frage und Antworten) sowie die Button- und Progress-Marker entsprechend angezeigt werden. 

* GameFragment:
	* Ein Klick auf einen Progress-Indikator (``progressIndicators`` ist die Liste all dieser Views) soll die neue GameViewModel-Funktionalität verwenden, um zur entsprechenden Frage zu navigieren.


### Teil 2: Auswahl einer Quiz-Kategorie

Bei Klick auf den ``Start``-Button (StartFragment) soll nicht gleich ein Quiz gestartet werden, sondern ein Zwischenbildschirm zur Auswahl einer Quiz-Kategorie angezeigt werden (analog zum Beispiel ``02_layout``).

Ein Klick auf eine der vier angezeigten Kategorien startet ein Quiz mit Fragen aus der gewählten Kategorie.

#### Tasks:

* navigation_graph:
	* Hinzufügen einer neuen Destination ``CategoryScreenFragment`` inklusive Layout-File.
		* Die vier möglichen Kategorien sind "General", "History", "Science" und "Computer".
		* Die entsprechenden Icons sind in den ``drawable`` Ressourcen bereits vorhanden. 
		* Als Hintergrund für die Kategorie-Kacheln kann die bereits vorhandene Ressource ``category_background_border.xml`` verwendet werden. Sie können die Kacheln aber auch gerne nach Geschmack gestalten :) 
	* Ändern der Navigationspfade
		* von StartFragment soll es einen Pfad zu CategoryScreenFragment geben.
		* von CategoryFragment soll es einen Pfad zu GameFragment geben
		* von StartFragment zu GameFragment soll es keinen Pfad mehr geben. 
* Rest Schnittstelle:
	* Finden Sie auf der [Trivia Database Homepage](https://opentdb.com/) im Menüpunkt "API" heraus
		* wie man nur Fragen einer bestimmten Kategorie vom API bekommt (die restlichen Parameter - 10 Fragen, nur MultipleChoice - bleiben gleich)
		* welche Kategorien es gibt und welche ID verwendet werden muss (es sollen die Kategorien "General Knowledge", "History", "Science & Nature" und "Science: Computers" verwendet werden).
* TriviaDbApi:
	* Erweitern Sie das API um eine neue Methode ``suspend fun getQuestionsForCategory(@Query("category") categoryId: Int): Response<QuestionsResponse>``.
* StartFragment:
	* Bei Klick auf den Start-Button soll *nicht* gleich ein Quiz gestartet werden. 
	* Dafür soll anstatt zum GameFragment zum CategoryChooserFragment navigiert werden.
* QuestionRepository:
	* Ändern Sie die Methode ``getQuestions``, indem Sie sie um einen Parameter ``categoryId`` (Int) erweitern.
	* Diese ``categoryId`` soll als Parameter der neuen Methode von TriviaDbApi verwendet werden, um Fragen nur einer Kategorie zu laden. 
* GameViewModel:
	* Erweitern Sie die ``start`` Methode um einen Parameter für die Kategorie, für welche das Quiz gestartet werden soll. 
* CategoryChooserFragment:
	* Bei Klick auf eine der Kategorien soll
		* ein Quiz für diese Kategorie gestartet werden
		* zum GameFragment navigiert werden.


### Abgabe

Legen Sie Ihr Projekt als zip-Datei in Moodle unter "Abgabe Assignment" ab (Abgabe bis spätestens 01.07. 08:30 möglich). 