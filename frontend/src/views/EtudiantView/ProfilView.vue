<template>
  <div class="profil-container">
    <h2>Profil de {{ etudiant.nom }} {{ etudiant.prenom }}</h2>
    <p><strong>Promotion :</strong> {{ etudiant.promotion }}</p>
    <p><strong>Email :</strong> {{ etudiant.email }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const etudiant = ref({})
const ETUDIANTS_URL = 'http://localhost:8989/api/etudiants'

// Charger les informations de l'étudiant sélectionné
const getEtudiant = () => {
  const idEtudiant = sessionStorage.getItem('selectedEtudiant')
  if (!idEtudiant) return

  fetch(`${ETUDIANTS_URL}/${idEtudiant}`)
    .then(response => response.json())
    .then(data => {
      etudiant.value = data
    })
    .catch(error => console.error("Erreur lors du chargement du profil :", error))
}

onMounted(getEtudiant)
</script>

<style scoped>
.profil-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}
</style>
