<template>
  <div class="main-content">
    <!-- 📌 Titre -->
    <h1 class="title">Attribution des points</h1>

    <!-- 📅 Filtres -->
    <div class="filters">
      <div class="filter-group">
        <label>Date début</label>
        <input type="date" class="date-input" v-model="dateDebut" />
      </div>
      <div class="filter-group">
        <label>Date fin</label>
        <input type="date" class="date-input" v-model="dateFin" />
      </div>
      <div class="filter-group">
        <label>Année universitaire</label>
        <select v-model="anneeUniversitaire" class="select-input">
          <option value="2024/2025">2024/2025</option>
          <option value="2023/2024">2023/2024</option>
          <option value="2022/2023">2022/2023</option>
        </select>
      </div>
    </div>

    <!-- 📊 Tableau -->
    <div class="table-container">
      <table class="styled-table">
        <thead>
          <tr>
            <th>Nom</th>
            <th>Promotion</th>
            <th>Type engagement</th>
            <th>Résumé contenu</th>
            <th>Validé</th>
            <th>NB points</th>
            <th>Remarque</th>
            <th>Points accordés</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(eleve, index) in eleves" :key="index">
            <td>{{ eleve.nom }}</td>
            <td>{{ eleve.promotion }}</td>
            <td>
              <input type="text" v-model="eleve.typeEngagement" class="editable-input" />
            </td>
            <td>
              <input type="text" v-model="eleve.resume" class="editable-input" />
            </td>
            <td>
              <label class="custom-checkbox">
                <input type="checkbox" v-model="eleve.valide" class="hidden-checkbox" />
                <span class="checkmark"></span>
              </label>
            </td>
            <td>
              <input type="number" v-model="eleve.nbPointsEnvisages" class="editable-input" />
            </td>
            <td>
              <span>{{ eleve.remarqueReferent }}</span>
            </td>
            <td>
              <input type="number" v-model="eleve.pointsAccordes" class="editable-input" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 📩 Bouton d’envoi -->
    <div class="button-container">
      <button class="btn-submit" @click="validerEnvoi(showModal)">
        Envoyer au service de scolarité
      </button>
    </div>

    <!-- ✅ Modale de confirmation -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal">
        <h3>✅ Accès activé !</h3>
        <p>Le service de scolarité peut maintenant visualiser les fiches avec les points attribués.</p>
        <button class="modal-btn" @click="showModal = false">OK</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { ELEVES, validerEnvoi } from "../../constante/AttribuerPointsViewConstants";

const eleves = ref(ELEVES); // Déclarez eleves comme une référence
const dateDebut = ref("");
const dateFin = ref("");
const anneeUniversitaire = ref("2024/2025");
const showModal = ref(false);
</script>

<style scoped src="./AttribuerPointsView.css"></style>