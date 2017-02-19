VOP Groep 1 2014-2015
=====================
Members:
- Mathias De Brouwer (Mrdbrouw.DeBrouwer@UGent.be)
- Harm Delva (Harm.Delva@UGent.be)
- Maxime Fernández Alonso (Maxime.FernandezAlonso@UGent.be)
- Jens Spitaels (Jens.Spitaels@UGent.be)
- Casper Van Gheluwe (Casper.VanGheluwe@UGent.be)
- Steven Van Maldeghem (Steven.VanMaldeghem@UGent.be)
- Titouan Vervack (Titouan.Vervack@UGent.be)

[Timesheets](https://docs.google.com/spreadsheets/d/1QN67IEwNJGiHO5mEH73o1j1MOAG34u-o3yinJIat0zw/edit?usp=sharing)

[Opgave website](https://github.ugent.be/pages/VakOverschrijdendProject/)

[Website](https://vopro1.ugent.be)

[Jenkins](https://vopro1.ugent.be/jenkins)

[Sonarqube](https://vopro1.ugent.be/sonar)

[Slack](https://vopgroep1.slack.com/messages/general/)

[Swagger](http://vopro1.ugent.be/swagger)

Build Status
============
* Master: [![Build Status](https://vopro1.ugent.be/jenkins/job/Master/badge/icon)](https://vopro1.ugent.be/jenkins/job/Master/)
* Development: [![Build Status](https://vopro1.ugent.be/jenkins/job/Development/badge/icon)](https://vopro1.ugent.be/jenkins/job/Development/)
* Other branches: [![Build Status](https://vopro1.ugent.be/jenkins/job/Any%20branch/badge/icon)](https://vopro1.ugent.be/jenkins/job/Any%20branch/)

Credentials
===========
De standaard is:

    username: minerva_username
    wachtwoord: VOPVOP

Voor de /data gebruik je het volgende:

    username: VOPdata
    wachtwoord: VOPIsC001

Voor de /sonar gebruik je het volgende:

    username: VOPsonar
    wachtwoord: VOPIsC001

Branching Strategy
==================
We maken gebruik van een master en development branch, deze blijven altijd bestaan. We maken ook gebruik van feature en hotfix branches, dit alles staat [hier](http://nvie.com/posts/a-successful-git-branching-model/) beschreven.

Voor de leesbaarheid te verbeteren vervangen we de eerste `-` door een `/` en hernoemen we `hotfix` naar `fix/issue-id`, dit wil dus ook zeggen dat bugs een issue moeten hebben.

Tenslotte zijn er ook nog gewoon issue branches, als je issues oplost die geen bug zijn, kan je gewoon een issue branch aanmaken met die begint met de issue-id.

TL;DR:
- master branch
- development branch
- feature/* branches (bvb. `feature/DAO-base`)
- fix/* branches (bvb. `fix/132/crashing-button` 132 is hier de issue-id en `crashing-button` een beschrijving van de issue)
- issue-id/* branches (bvb. `131/random-issue` 131 is hier de issue-id en `random-issue` een beschrijving van de issue)

Branching Flow
==============
Feature en fix en issue branches worden gemerged naar de development branch, dit gebeurt d.m.v. een pull request (die je liefst niet zelf approved).

De development branch is eigenlijk de bleeding-edge versie van onze software, eens deze stabiel en getest is, wordt er naar master gemerged (liefst altijd door dezelfde persoon).
