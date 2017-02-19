---
title: DIAGRAMS
---
# DIAGRAMS #
<br>
<p>
Hier vindt u een vereenvoudigde, volledige klassediagram van de applicatie. Deze biedt u een mooi overzicht over hoe de packages van elkaar afhankelijk zijn.
Verder is er voor elke package ook een klassediagramen en voor de volledigheid is er ook een volledige UML klassediagram van de applicatie, die alle klassen toont.
Aansluitend geven we ook een EER diagram weer die de achterliggende databank voorstelt.
Ook zijn er twee sequentiediagrammen voorzien die een typische interactie met de software voorstellen, als voorbeeld hebben we het toevoegen van een project genomen. 
Hierbij wordt het verloop van een succesvolle toevoegoperatie getoond alsook het toevoegen van een project met een reeds bestaande naam, wat voor een error zal zorgen. 
Alle andere interacties (ook voor concepten, actoren en usecases) verlopen op een analoge manier.
Er wordt ook een deploymentdiagram, een componentdiagram en, voor een beter overzicht, een permissiediagram voorzien.
</p>
<select id="diaDrop" onChange="run()">
    <option value="vopro1.png"> 1. Volledig vereenvoudigd overzicht </option>
    <option value="action.png"> 1.1 UML diagram: Action </option>
    <option value="adapter.png"> 1.2 UML diagram: Adapter </option>
    <option value="converter.png"> 1.3 UML diagram: Converter </option>
    <option value="interactor.png"> 1.4 UML diagram: Interactor </option>
    <option value="model.png"> 1.5 UML diagram: Model </option>
    <option value="persistence.png"> 1.6 UML diagram: Persistence </option>
    <option value="reference.png"> 1.9 UML diagram: Reference </option>
    <option value="rest.png"> 1.10 UML diagram: REST </option>
    <option value="rest_with_presentation_models.png"> 1.11 UML diagram: REST (met presentation models) </option>
    <option value="scheduling.png"> 1.12 UML diagram: Scheduling </option>
    <option value="story.png"> 1.13 UML diagram: Story </option>
    <option value="vopro1_full.png"> 1.14 Volledige UML diagram </option>
    <option value="eer.png"> 2. EER diagram </option>
    <option value="UML_sequence_diagram_success.png"> 3. Sequentiediagram (Success)</option>
    <option value="UML_sequence_diagram_nosuccess.png"> 4. Sequentiediagram (No Success) </option>
    <option value="Deployment.png"> 5. Deploymentdiagram </option>
    <option value="Component_Diagram.png"> 6. Componentdiagram </option>
    <option value="PermissionDiagram.png"> 7. Permissiediagram </option>
</select>

<a id='diaLink' href="http://vopro1.ugent.be/data/img/vopro1.png"><img id="diaImg" src="/data/img/vopro1.png" width="90%"></a>

<script type="text/javascript">
  function run() {
    var dropdown = document.getElementById("diaDrop");
    var selectedCase = dropdown.options[dropdown.selectedIndex].value;
    var linkName = "http://vopro1.ugent.be/data/img/".concat(selectedCase);
    if (selectedCase == "vopro1_full.png") {
      selectedCase = "toobig.png";
    }
    var diaName = "/data/img/".concat(selectedCase);
    document.getElementById("diaImg").src=diaName;
    document.getElementById("diaLink").href=linkName;
    
    if (selectedCase == "Deployment.png" || selectedCase == "Component_Diagram.png" || selectedCase == "PermissionDiagram.png" || selectedCase == "toobig.png") {
      document.getElementById("diaImg").style.width = "auto"
    } else {
      document.getElementById("diaImg").style.width="90%"
    }
  }
</script>