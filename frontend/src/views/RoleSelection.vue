<template>
  <div class="role-selection">
    <h2>Choisissez votre rôle</h2>
    <div class="buttons-container">
      <button @click="goToEtudiantSelection">Étudiant</button>
      <button @click="goToDirecteur">Directeur </button>
      <button @click="goToReferent">Référent </button>
      <button @click="goToServiceScolarite">Service Scolarité </button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToEtudiantSelection = () => {
  router.push('/selection-etudiant') // Redirection vers la sélection d'étudiant
}

const goToDirecteur = () => {
  router.push('/directeur') // Redirection vers la page directeur
}

const goToReferent = () => {
  router.push('/referent') // Redirection vers la page référent
}
const goToServiceScolarite = () => {
  router.push('/service-scolarite') // Redirection vers la page service scolarité
}
</script>

<style scoped>
.role-selection {
  text-align: center;
  margin-top: 50px;
}

.buttons-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
  max-width: 300px;
  margin: auto;
}

button {
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  background-color: #6C3EA9;
  color: white;
  border: none;
  border-radius: 5px;
}

button:hover {
  background-color: #532a80;
}

button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
