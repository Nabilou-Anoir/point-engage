<template>
  <div class="selection-container">
    <h2>Sélectionnez un étudiant</h2>

    <!-- Sélecteur d'étudiants -->
    <select v-model="selectedEtudiant" @change="selectEtudiant">
      <option disabled value="">Veuillez choisir un étudiant</option>
      <option v-for="etudiant in etudiants" :key="etudiant.idEtudiant" :value="etudiant.idEtudiant">
        {{ etudiant.nom }} {{ etudiant.prenom }}
      </option>
    </select>

<!--    &lt;!&ndash; Affichage du nom de l'étudiant sélectionné &ndash;&gt;-->
<!--    <p v-if="selectedEtudiant">Étudiant sélectionné : {{ getSelectedEtudiantName() }}</p>-->
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const etudiants = ref([]) // Liste des étudiants
const selectedEtudiant = ref(sessionStorage.getItem('selectedEtudiant') || '') // Étudiant sélectionné

const ETUDIANTS_URL = 'http://localhost:8989/api/etudiants'

// Charger la liste des étudiants
function getEtudiants() {
  fetch(ETUDIANTS_URL)
    .then(response => response.json())
    .then(data => { etudiants.value = data })
    .catch(error => console.log("Erreur lors du chargement des étudiants :", error))
}

// Enregistrer l'étudiant sélectionné et rediriger vers l'accueil
function selectEtudiant() {
  const etudiant = etudiants.value.find(e => e.idEtudiant == selectedEtudiant.value)
  if (etudiant) {
    // Sauvegarde ID + Nom et Prénom pour affichage dans les notifications
    sessionStorage.setItem('selectedEtudiant', selectedEtudiant.value)
    sessionStorage.setItem('selectedEtudiantName', `${etudiant.nom} ${etudiant.prenom}`)
  }
  router.push('/etudiant') // Redirection vers la page d'accueil étudiant
}

// // Trouver le nom de l'étudiant sélectionné
// function getSelectedEtudiantName() {
//   return sessionStorage.getItem('selectedEtudiantName') || ""
// }

onMounted(getEtudiants)
</script>

<style scoped>
.selection-container {
  text-align: center;
  margin-top: 50px;
}

select {
  width: 100%;
  padding: 10px;
  font-size: 16px;
}

p {
  margin-top: 10px;
  font-weight: bold;
}
</style>
