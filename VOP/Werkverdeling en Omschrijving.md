# Werkverdeling
#####Steven
De adapters zelf. Hier moet heel veel casting gebeuren en zo, sowieso genoeg werk mee.

#####Maxime
Mappers, in de adapterlaag moet er omgezet worden van Entity naar PresentationModel. Deze klassen hebben we nog nodig (wat erin moet zitten staat in de REST API). Steven had al een beetje gewerkt aan de exceptions mappen, daar kan je verder aan werken. Misschien iets zoeken tegen de code duplication ofzo, beetje overleggen met Steven.

In de REST laag moet er nog mapping gebeuren naar ResponseEntities, dus volledig met Status Code en zo. Hiervoor kan je eventueel samenwerken met Matthias, die zijn REST Controllers zijn al bijna af, en die dingen zijn verbonden met elkaar.

#####Jens
Goed bezig met de JSON converters, misschien de entities wat aanvullen (database-id velden). De bijhorende DAO implementeren? Deze zal niet veel doen buiten jouw converter aanroepen. Sample code staat in de vorige DAO branches, JdbcTemplate en zo gebruiken omdat dat ding magisch is.

####Mathias
REST Controllers, als die af zijn eventueel beginnen aan de mapping naar de ResponseEntities.

####Titouan
Interactors, misschien eens overleggen met Steven hoe de interfaces er ideaal zouden uitzien. De project naam in ieder geval niet vergeten. Vanaf de DAO werkt kan je alles al beginnen implementeren zonder de funky, ik zal hopelijk zo rap mogelijk alle mogelijke dingen in FUnky gekregen hebben om dan de andere dingen toe te voegen. 

####Casper
Je kan denk ik al bijna beginnen aan de test klassen, iedere laag moet apart kunnen getest worden dus gaan ze stubs/drivers nodig hebben. Die gaan denk ik nog veel werk zijn jammer genoeg.

### DAO
De database zal er anders uitzien dan origineel gepland. Alles voor deze milestone zit in 1 table. Deze table heeft 3 kollomen, id, name en 'blob'. Het id veld is iets dan enkel gebruikt wordt in de database en zal nooit aangepast worden. Dit in tegenstelling tot de naam. Dit zullen we nodig hebben voor de update method. Het naam veld is de Fully Qualified Name van de entity, dus samen met zijn namespace en moet ook uniek zijn over heel deze table. Het blob veld kan van gelijk welk formaat zijn, maar bij ons waarschijnlijk gewoon json. Dit encapsuleert alle informatie die nodig is voor de entity opnieuw uit te lezen. Onderandere een veld 'type' dat het soort Entity bevat.
##### Conversies
Met zo'n blob zijn we uiteraard niks, dit moet omgezet worden naar een Entity. Hiervoor hebben we dus een converter nodig. Ook omgekeerd, als we een Entity willen opslaan moet hier eerst een blob van gemaakt worden. Dezelfde converter zal dit doen zodat alles hopelijk consistent blijft. 

De interface van de DAO zal denk ik een save/update definiëren voor ieder entity. Daarna de gepaste call doen naar de converter, een String terug krijgen en het geheel opslaan.

De getByName en getByID (de DocumentLoader zal dit nodig hebben) zijn iets minder evident. Return type behoort niet tot de signature van een method, dus kunnen we niet echt iets overloaden hieraan. Ik zou voorstellen om slechts 1 method per soort get te definieren, en deze een Declaration te laten returnen. Het object dat de DAO aanspreekt zal dan moeten casten naar het soort object dat hij wou. De bijhorende excepties zullen uiteraard moeten worden opgevangen.


### Presentation Model  < - >  Entity
Het zal denk ik wel handig zijn om een onderscheid te maken tussen Entities en hun Presentation model. 
Entities zullen onder andere hun database id proberen bij te houden, zodat een update methode in de DAO enkel een entity object nodig heeft. Het presentation model zal alle velden hebben die de REST API wilt dat we teruggeven. Deze objecten zullen dan liefst ook de toString overriden, of een toJSON definiëren zodat dit simpel terug te geven is. 

De UseCase Entities zullen denk ik enorm verschillen van het formaat dat de REST API wilt. We moeten alle gebruikte concepten bijvoorbeeld in een array steken binnen de UseCase zelf. Voor te valideren lijkt het mij echter veel logischer gebruikte referenties in de steps op te slaan. De ResultMapper in de adapterlaag zal vermoedelijk alle steps moeten overlopen en Concepten eruit halen en in een aparte lijst steken. En ondertussen ook de executors per step aflopen en die steken in de array van actors.

# Web & Rest Laag
Deze doen zo weinig mogelijk. Alle requests worden gemapt op 1 method, de request parameters en path variables worden in 1 HashMap gestoken. De keys in deze HashMap komen overeen met die de REST API specifieert, (zit in /Data op geethub).
Daarna wordt de HashMap doorgegeven aan de/een AdapterManager. 

De REST Controller zal een Result object terug krijgen, dat dan in zijn geheel doorgegeven wordt aan een Mapper, die een ResponseEntity zal maken ervan. De response codes zijn denk ik niet volledig gedefinieerd in de API zelf, dus dit zal nog moeten uitgepluisd worden denk ik? Ik vermoed ook dat er een apart Result type zal moeten zijn voor succesvol verwijderen, aangezien deze aan andere code (204?) nodig heeft.

## Adapter

Centraal in de adapterlaag komt de AdapterManager. Deze is het aanspreekpunt vanuit de REST laag, en heeft een method per functie die we ondersteunen. Ik denk dat we hiervoor wel nog alles in 1 klasse/object kunnen steken omdat hier echt niks gedaan wordt. De manager zal werk delegeren naar de juiste adapters die al het effectieve werk zullen.

Binnenkomende requests zullen hier dus omgezet worden in een nuttig formaat. Dit formaat wordt dan gebruikt voor de interactor om zijn dingen ermee te doen, dus dit moet zo nuttig mogelijk zijn. De inkomende HashMap zal van het formaat <String, Object> zijn. De key values staan in de rest api op github (/Data, steken in swagger.io). Veel casting magic. Ofwel zit hier een Array<String> (HOOP IK, als spring doet wat ik denk dat het doet) of een String in, dus dat is de eerste cast. Dan de contents casten naar het juiste type, dat gaat meestal gewoon String blijven.

Ik zou voorstellen om alles om te zetten naar Entity objecten. Soms zullen een groot deel van de velden dan leeg zijn, maar alle method signatures zullen wel redelijk uniform zijn. Bijvoorbeeld voor het aanpassen van een Concept. Een Concept entity zal 4 velden hebben, id/naam/definitie/attributes. Het formaat dat dan aan de interactor gegeven wordt zal uit 2 Concept objecten en 1 String bestaan. Het eerste Concept zal enkel een ingevuld naam veld hebben, het tweede zal alles ingevuld hebben behalve het id veld. De String zal voor de project naam staan. Hier kunnen we denk ik de Project uit Funky niet gewoon aanmaken, want dit heeft LanguageRepositories n shit nodig, echt niet praktisch. 

Ook het resultaat zal hier omgezet worden van formaat, hierbij kunnen er 2 dingen voorvallen:
- Er zijn geen problemen opgetreden, alles is verlopen zoals gepland. Dan moet de ResultMapper aangesproken worden om van het resulterende Entity een Result object te maken. Dit result object zal een enum zijn met zijn type informatie en het Presentation Model van deze entity bevatten. Er zal dus een omzetting moeten gebeuren, dat vooral bij UseCase objecten niet trivaal zal zijn. Meer uitleg staat ergens in het begin van het document.
- Het kan ook zijn dat er iets fout gegaan is. Alle mogelijke excepties worden hier dus opgevangen, omdat dit de enige laag is die weet welke soort excepties er allemaal kunnen optreden (vanuit de database). Dus alle mogelijke DataAccessExceptions, LookupExceptions (een declaration opvragen uit funky die niet bestaat), type cast excepties (omdat de DAO vermoedelijk Declaration objecten zal returnen die moeten gecast worden), en eventueel nog andere soorten die we later zullen ontdekken. De content van deze Results zijn nog een mysterie. Volgens de API moeten deze ook 3 velden hebben, onze API beheerder zal deze velden eens moeten uitleggen denk ik.

## Interactor
Interactors zorgen ervoor dat alle dingen die je kan uitsteken met de entities in de juiste volgorde gebeuren. Alle interactors zullen een Workspace object nodig hebben, en een DAO. Van Workspace zou ik liefst een singleton maken, want alle interactors moeten wel hetzelfde Workspace object bevatten. De DAO's kunnen denk ik vrij simpel met een XML config file aangemaakt worden (zoals in de vorige generatie DAO die op github staan). Spring Data beheert de database connectie, wij moeten enkel een DAO daar aan hangen en dat zou moeten werken. 

De werkmethode zal per Interactor verschillen. De meeste interacties zijn uniek per project, dus deze gaan beginnen met het opvragen van het juiste Project via de Workspace referentie en de String voor de project naam die hij binnen gekregen heeft. Afhankelijk van de interactor zal er dan iets in deze stijl gebeuren:
- Indien er iets moet opgeslaan worden wordt nu de save method van de DAO opgeroepen. Daarna misschien nog een find method om het opgeslaan object terug op te halen, want we zullen het id nodig hebben. Daaarna wordt in het project een Entity van het correcte type aangemaakt. Hiervoor maak ik hopelijk nog nieuwe methods zodat je gewoon een Entity kan doorgeven eraan ipv alles opnieuw aan te maken.
- Iets updaten zal eerst het oude object opvragen aan de database, het id uit dit object zal in het object dat de nieuwe informatie voorstelt gestoken worden en dan wordt de update method opgeroepen. Daarna moet de cache van het Project geflusht worden. Bij de volgende milestone zal er ook iets van refactoring bijmoeten dan, alle references naar deze declaration opvragen en aanpassen. Iets met casting naar NameReference, problemen voor later.
- Verwijderen is nog een mysterie denk ik. Vermoedelijk zal ik aan de DocumentScanner moeten iets toevoegen om een Loader te verwijderen op basis van het ID waar hij verantwoordelijk voor is. Daarna zou het moeten volstaan de entry uit de database te verwijderen, en de cache te flushen.

## Entities
Deze hebben ID's nodig, voor de rest heeft Jens al een groot deel geïmplementeerd denk ik.




# Feedback van de prof
Ons origineel idee van Funky Actor en ActorEntity te scheiden valt weg. De interactors zullen bvb enkel werken met de entities die ook in Funky gebruikt worden (en dus overven van Declaration). Deze objecten kunnen perfect zonder Document ofzo overleven, dus dat is wel handig.

Wat we wel nog zullen kunnen gebruiken zijn zogezegde Presentation Models. Denk aan wat het verschil is tussen een UseCase object voor funky (geneste boom magic), en wat we waarschijnlijk terug gaan willen geven aan de web client. Voor de andere Entities is dit verschil veel kleiner, maar we gaan dit toch ook implementeren denk ik? Deze leven vooral in de Web en Adapterlaag, meer uitleg bij de adapterlaag.

RIP DAO's. En EER-diagram. Het zou zo te zien beter zijn om alles als json blob op te slaan. Concreet betekent dit, alle entities in deze milestone zullen in 1 tabel leven. Dit zal handig zijn, want 1 DAO -> 1 type DocumentScanner -> 1 type ProjectBuilder -> ... Die tabel zal 3 velden hebben (denk ik). Een kolom id, een Fully Qualified Name en de json blob. Het id wordt enkel en alleen gebruikt voor da database zelf. Als je een entry's naam wil aanpassen wil je natuurlijk dat dezelfde entry aangepast wordt, dus moet er iets constant blijven. De Fully qualified name is de namespace+naam van dit object, da wordt nog een beetje uitzoeken of funky dat zelf in stukken snijdt of niet. Op basis hiervan kunnen we later normaal lazy loading erin steken. Normaal gezien. De blob zelf zal custom made zijn, want er zullen extra dingen inmoeten. Voorlopig denk ik alleen een extra veld voor het type, wat duidelijk belangrijk zal zijn om er een deftig object weer uit te krijgen.

Deze conversie moet volledig in de persistentie laag gebeuren, wat bij ons ongeveer overeenkomt met de adapter laag. We gaan dus een converter nodig hebben dat een json blob omzet in een Entity, waarschijnlijk met generic magic? Dit wordt waarschijnlijk een aparte job voor iemand, vrij belangrijk en nog veel werk aan denk ik. Voordeel is wel dat de UseCase Entities zelf vrij analoog zullen zijn als de rest, so that's nice.

Een Concept/Actor/... opvragen zal niet echt meer via de DAO gaan. De DAO gaat deze methods wel nog nodig hebben voor de Scanner en Loader, maar de Interactors vragen hun shit normaal op via de namespace().find() methode. Meer uitleg over hoe dit werkt staat in mijn github issue, en in de mail die de prof gestuurd had (convenience methods ofzo). 
