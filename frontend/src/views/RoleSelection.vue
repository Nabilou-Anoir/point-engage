<!-- src/views/RoleSelection.vue -->
<template>
  <div class="role-selection">
    <h1>Choisissez votre rôle</h1>
    <div class="buttons">
      <button @click="goToEtudiant">Étudiant</button>
      <button @click="goTo('directeur')">Directeur</button>
      <button @click="goTo('referent')">Référent</button>
      <button @click="goTo('service-scolarite')">Service Scolarité</button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

function goToEtudiant() {
  router.push('/etudiant')
}

function goTo(role) {
  router.push(`/${role}`)
}
</script>

<style scoped>
.role-selection {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.buttons {
  display: flex;
  gap: 20px;
  margin-top: 20px;
}

button {
  padding: 10px 20px;
  font-size: 18px;
  cursor: pointer;
  border: none;
  background-color: #6C3EA9;
  color: white;
  border-radius: 4px;
  transition: background-color 0.2s ease;
}

button:hover {
  background-color: #532a80;
}
</style>
