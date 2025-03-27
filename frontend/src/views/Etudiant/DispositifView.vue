<!-- src/views/DispositifView.vue -->
<template>

  <div class="dispositif-container">
    <h1>Dispositif Ingénieur Engagé</h1>

    <section class="intro">
      <h2>Une vision humaniste de l’ingénieur</h2>
      <p>
        En s’appuyant sur des valeurs fédératrices – diversité, ouverture, égalité, solidarité – l’école d’ingénieurs ISIS a vocation à former des ingénieurs engagés, responsables et citoyens.
        Ces valeurs sont promues à travers des enseignements (éthique, interculturalité, enjeux socio-environnementaux, etc.) et de nombreuses activités proposées aux élèves tout au long de leur cursus (missions humanitaires, activités associatives, formations, conférences-débats, etc.).
      </p>
      <p>
        Les élèves-ingénieurs ISIS sont incités à s’engager dans des actions porteuses de sens et contribuant à promouvoir les valeurs de l’ingénieur humaniste. Pour ce faire, ISIS propose le dispositif Ingénieur Engagé.
      </p>
    </section>

    <section class="actions">
      <h2>Les actions de l’Ingénieur Engagé</h2>
      <p>Quatre types d’actions sont reconnus dans ce dispositif :</p>
      <ul>
        <li>Engagement Associatif</li>
        <li>Engagement Diversité</li>
        <li>Engagement Climat-Environnement</li>
        <li>Engagement École</li>
      </ul>
    </section>

    <section class="points">
      <h2>Les points Ingénieur Engagé</h2>
      <p>
        Chaque action effectuée par l’élève se voit attribuer un nombre de points, cumulables dans le cadre d’un semestre.
        Ces points peuvent être ajoutés à la note obtenue dans une Unité d’Enseignement.
        Attention, le nombre total de points par semestre ne peut dépasser 0,5 point.
      </p>
    </section>

    <section class="process">
      <h2>Calendrier et processus</h2>
      <p>
        L’action doit être validée par la direction des études. L’élève doit soumettre la fiche descriptive de l’action au plus tard 15 jours avant le jury de semestre. Pour toute autre information, vous pouvez contacter l’adresse mail suivante :
        <a href="mailto:ingenieur-engage-isis@listes.univ-jfc.fr">ingenieur-engage-isis@listes.univ-jfc.fr</a>.
      </p>
      <p>
        Les Points Ingénieur Engagé sont ensuite vérifiés et validés par la direction des études.
      </p>
    </section>

    <section class="correspondances">
      <h2>Tableau des correspondances Actions / Points</h2>
      <table class="correspondence-table">
        <thead>
        <tr>
          <th>Catégorie</th>
          <th>Action</th>
          <th>Points associés</th>
        </tr>
        </thead>
        <tbody>
        <!-- Engagement Associatif -->
        <tr>
          <td rowspan="3">Engagement Associatif</td>
          <td>Membre du bureau d'une association</td>
          <td>Entre 0,1 et 0,4</td>
        </tr>
        <tr>
          <td>Autre contribution sur le campus</td>
          <td>Entre 0,1 et 0,25</td>
        </tr>
        <tr>
          <td>Contribution hors campus</td>
          <td>Entre 0,1 et 0,25</td>
        </tr>
        <!-- Engagement Diversité -->
        <tr>
          <td rowspan="3">Engagement Diversité</td>
          <td>Tuteur "La main à la pâte"</td>
          <td>0,35</td>
        </tr>
        <tr>
          <td>Missions proposées par l'AFEV</td>
          <td>Entre 0,1 et 0,25</td>
        </tr>
        <tr>
          <td>Aide aux devoirs</td>
          <td>Entre 0,1 et 0,25</td>
        </tr>
        <!-- Engagement Climat-Environnement -->
        <tr>
          <td rowspan="3">Engagement Climat-Environnement</td>
          <td>Animation de "la fresque du climat"</td>
          <td>0,15 (par atelier)</td>
        </tr>
        <tr>
          <td>Animation de "la fresque du numérique"</td>
          <td>0,15 (par atelier)</td>
        </tr>
        <tr>
          <td>Actions sur les enjeux Climat-Env.</td>
          <td>Entre 0,1 et 0,25</td>
        </tr>
        <!-- Engagement École -->
        <tr>
          <td rowspan="4">Engagement École</td>
          <td>Ambassadeur ISIS</td>
          <td>Entre 0,1 et 0,4</td>
        </tr>
        <tr>
          <td>Animation des journées Portes Ouvertes et Métiers</td>
          <td>Entre 0,1 et 0,25</td>
        </tr>
        <tr>
          <td>Préparation et animation des évènements de l'école</td>
          <td>Entre 0,1 et 0,25</td>
        </tr>
        <tr>
          <td>Participation aux instances universitaires</td>
          <td>Entre 0,1 et 0,25</td>
        </tr>
        </tbody>
      </table>
    </section>
  </div>
</template>

<script setup>
</script>

<style scoped>
.dispositif-container {
  max-width: 900px;
  margin: 2rem auto;
  padding: 1rem;
  font-family: Arial, sans-serif;
  color: #333;
  line-height: 1.6;
}

.dispositif-container h1 {
  text-align: center;
  color: #6C3EA9;
  margin-bottom: 1rem;
}

.dispositif-container h2 {
  color: #6C3EA9;
  margin-top: 1.5rem;
  margin-bottom: 0.5rem;
}

.dispositif-container p {
  margin-bottom: 1rem;
  text-align: justify;
}

.dispositif-container ul {
  list-style-type: disc;
  padding-left: 1.5rem;
  margin-bottom: 1rem;
}

.dispositif-container a {
  color: #6C3EA9;
  text-decoration: none;
}

.dispositif-container a:hover {
  text-decoration: underline;
}

.correspondence-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.correspondence-table th,
.correspondence-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.correspondence-table th {
  background-color: #f4f4f4;
  color: #333;
}
</style>
