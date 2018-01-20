# ControlApplication
Repository for the Robocode-Controlling Application

Funktionweise:
Allgmeine:
  . Der Roboter macht nichts anderes, als seine Gun um 360° zu drehen, und dann abhängig, ob in seine DNA für dieses Entdeckung des Gegeners eine 1 oder eine 0 steht, ein Bullet abzufeuern
  - Dazu wird am Anfang eine DNA-Pool mit 10 zufälligen DNA-Sequzenzen erzeugt (zufälligen 1en und 0en)
  - bei Beginn jeder Runde, wird die DNA des Robots aus diesem DNA-Pool erzeugt 
  - mmer nur ein Robot der Population kämpft gerade, aber es wird mit einer Population von 10 gearbeitet 
    -> werden 10 Runden durchgespielt, dann ist die Generation beendet und ein neuer DNA-Pool wird erzeugt
  - der FitnessWert wird in der Control-Application berechnet (geht nur hier) und der Robot darf keine Referenz auf die Control-Application     haben (der Robot muss ein fertig kompiliertes JavaProgrammsein
    -> dazu wird der FitnessWert und der DNAPool und das erzeugen der DNA in der Control-Application getan, während der Robot nur bei jeder     Runde seine DNA als Txt-File einliest)


MainControl.java:
  - Über die Control-API von Robocode wird Robocode gestartet und ein Battle mit Generations*PopulationSize Runden gestartet mit dem           eigenen genetischen Roboter und einem Gegner (hier SittingDuck -> sitzt nur rum)
  - zudem wird eni Battleobserver hinzugefügt, der auf Events von Robocode hört
  
BattleObserverStandard
- wichtigsten beiden Events sind RoundEnded und RoundStarted
RoundStarted -> aus dem DNAPool.txt muss die DNA für den nun kämpfenden Roboter erstellt und in DNA.txt gespeichert werden, welcher der Robot dann einliesst
RoundEnded -> der FitnessWert des Robots wird ermittelt und mit der DNA in eine Sammlung der DNA Sequenzen dieser Generation in collectionDNA.txt abgespeichert
          -> wenn die Gernation beendet ist (Round % 9 == 0) -> alle 10 Runden 
            -> dann wird aus der Sammlung der DNA Sequenzen mit ihren Fitnes-Werten ein neuer DNA Pool für die nächste genration erzeugt

BattleObserverNTournament:
- Version, welche den nTournament Auswahl prozess implementiert

FileTools.java:
- ganzen Methoden, die in Files schriben und lesen

DnaOperations.java:
- die ganzen methoden, die was mit der DNA machen
- Auswahlprozess spezifische Methoden wurden in deren Unterpackages umgezogen

