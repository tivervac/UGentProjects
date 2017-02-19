---
title: USE-CASES
---
# USE-CASES #
<br>
<select id="usedrop" onChange="run()">
    <option value="usecase1"> 1. Inloggen in het systeem </option>
    <option value="usecase2"> 2. Uitloggen op het systeem </option>
    <option value="usecase3"> 3. Nieuw project aanmaken </option>
    <option value="usecase4"> 4. Project wijzigen </option>
    <option value="usecase5"> 5. Project verwijderen </option>
    <option value="usecase6"> 6. Nieuwe usecase aanmaken </option>
    <option value="usecase7"> 7. Use case wijzigen </option>
    <option value="usecase8"> 8. Use case verwijderen </option>
    <option value="usecase9"> 9. Use case valideren </option>
    <option value="usecase10"> 10. Nieuw concept aanmaken </option>
    <option value="usecase11"> 11. Concept wijzigen </option>
    <option value="usecase12"> 12. Concept verwijderen </option>
    <option value="usecase13"> 13. Nieuwe actor aanmaken </option>
    <option value="usecase14"> 14. Actor wijzigen </option>
    <option value="usecase15"> 15. Actor verwijderen </option>
    <option value="usecase16"> 16. Storyboard afprinten </option>
    <option value="usecase17"> 17. Filteren van objecten op basis van query </option>
</select>

<img id="caseImg" src="/data/img/usecase1.png">

<script type="text/javascript">
  function run() {
    var dropdown = document.getElementById("usedrop")
    var selectedCase = dropdown.options[dropdown.selectedIndex].value
    var imgName = "/data/img/".concat(selectedCase, ".png")
    document.getElementById("caseImg").src=imgName
  }
</script>
