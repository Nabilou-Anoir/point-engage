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

    <!-- Affichage du nom de l'étudiant sélectionné -->
    <p v-if="selectedEtudiant">Étudiant sélectionné : {{ getSelectedEtudiantName() }}</p>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const etudiants = ref([]) // Liste des étudiants
const selectedEtudiant = ref("") // Étudiant sélectionné

const ETUDIANTS_URL = 'http://localhost:8989/api/etudiants'

function getEtudiants() {
  fetch(ETUDIANTS_URL, { method: "GET" }) // Requête GET
    .then(response => response.json()) // Convertir la réponse en JSON
    .then(data => { etudiants.value = data }) // Mettre à jour la liste des étudiants
    .catch(error => console.log("Erreur lors du chargement des étudiants :", error)) // Gestion d'erreur
}

// Enregistrer l'étudiant sélectionné et rediriger vers l'accueil
function selectEtudiant() {
  sessionStorage.setItem('selectedEtudiant', selectedEtudiant.value) // Sauvegarde en session
  router.push('/etudiant') // Redirection vers la page d'accueil étudiant
}

// Trouver le nom de l'étudiant sélectionné
function getSelectedEtudiantName() {
  const etudiant = etudiants.value.find(e => e.idEtudiant == selectedEtudiant.value)
  return etudiant ? `${etudiant.nom} ${etudiant.prenom}` : ""
}

onMounted(getEtudiants) // Charger les étudiants au montage du composant
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
