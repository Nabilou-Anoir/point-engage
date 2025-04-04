<template>
  <div class="main-layout">
    <!-- Sidebar -->
    <aside class="sidebar">
      <div class="filter-options">
        <h2>Service Scolarité</h2>
      </div>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
      <h1 class="title">Liste des étudiants envoyée</h1>

      <div class="table-section">
        <div class="table-container">
          <!-- Affiche le tableau uniquement si isSent est vrai et qu'il y a des données -->
          <table class="styled-table" v-if="isSent && eleves.length">
            <thead>
            <tr>
              <th>Nom</th>
              <th>Promotion</th>
              <th class="align-right">Points accordés</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(eleve, index) in eleves" :key="index">
              <td>{{ eleve.name }}</td>
              <td>{{ eleve.promotion }}</td>
              <td class="align-right">
                <!-- Affichage en lecture seule des points -->
                <span>{{ eleve.points }}</span>
              </td>
            </tr>
            </tbody>
          </table>

          <!-- Message si aucune liste n'a été envoyée -->
          <div v-else>
            <p>Aucune liste n'a encore été envoyée.</p>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

const eleves = ref([]);
const isSent = ref(false);

onMounted(() => {
  // Vérifie si l'envoi a eu lieu via le flag 'isSent'
  if (sessionStorage.getItem("isSent") === "true") {
    isSent.value = true;
    const storedData = sessionStorage.getItem("elevesFiltres");
    if (storedData) {
      eleves.value = JSON.parse(storedData);
    }
  } else {
    isSent.value = false;
  }
});
</script>

<style scoped src="./ServiceScolarite.css"></style>
