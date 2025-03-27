<!-- http://localhost:5174/referent/accueil -->

<template>
  <div class="main-layout">
    <main class="main-content">
      <div class="top-filter">
        <div class="filter-group">
          <label class="selectlabel">Semestre</label>
          <div class="select-container">
            <select v-model="selectedSemestre" class="select-input">
              <option value="S1">Semestre 1</option>
              <option value="S2">Semestre 2</option>
            </select>
          </div>
        </div>
        <div class="filter-group">
          <label class="selectlabel">Année universitaire</label>
          <div class="select-container">
            <select v-model="selectedAnnee" class="select-input">
              <option value="2024/2025">2024/2025</option>
              <option value="2023/2024">2023/2024</option>
              <option value="2022/2023">2022/2023</option>
            </select>
          </div>
        </div>
        <div class="right-group">
          <div class="search-container">
            <input
              type="text"
              v-model="searchQuery"
              placeholder="Rechercher..."
              class="search-input"
            />
          </div>
        </div>
      </div>

      <div class="filter-group date-range">
        <div class="date-container">
          <label class="selectlabel">Date de début</label>
          <div class="select-container">
            <input
              type="date"
              v-model="dateDebut"
              class="select-input"
              placeholder="Date de début"
            />
          </div>
        </div>
        <div class="date-container">
          <label class="selectlabel">Date de fin</label>
          <div class="select-container">
            <input
              type="date"
              v-model="dateFin"
              class="select-input"
              placeholder="Date de fin"
            />
          </div>
        </div>
      </div>

      <h1 class="title">Référent</h1>

      <div class="table-section">
        <div class="table-container">
          <table class="styled-table">
            <thead>
              <tr>
                <th>Nom</th>
                <th>Promotion</th>
                <th>Type</th>
                <th>Fiche descriptive</th>
                <th>Validé par référent</th>
                <th>Remarque référent</th>
                <th>Envoyer</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(eleve, index) in eleves" :key="index">
                <td>{{ eleve.nom }}</td>
                <td>{{ eleve.promotion }}</td>
                <td>{{ eleve.typeEngagement }}</td>
                <td>
                  <span @click="toggleFicheVisible(eleve)" class="eye-icon">
                    {{ eleve.ficheVisible ? "👁️" : "👁️‍🗨️" }}
                  </span>
                </td>
                <td>
                  <input
                    type="checkbox"
                    v-model="eleve.valide"
                    class="styled-checkbox"
                  />
                </td>
                <td>{{ eleve.remarqueReferent }}</td>
                <td>
                  <input
                    type="checkbox"
                    v-model="eleve.envoye"
                    @change="validerEnvoi(eleve)"
                    class="styled-checkbox"
                  />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </main>
  </div>
</template>
  
<script setup>
import { ref, computed } from "vue";
import { ELEVES, validerEnvoi } from "../../constante/HistoriqueViewConstants";

const eleves = ref(ELEVES);
const searchQuery = ref("");
const selectedSemestre = ref("S1");
const selectedAnnee = ref("2024/2025");
const dateDebut = ref("");
const dateFin = ref("");

const toggleFicheVisible = (eleve) => {
  eleve.ficheVisible = !eleve.ficheVisible;
};

const elevesFiltres = computed(() => {
  let filteredEleves = eleves.value;

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    filteredEleves = filteredEleves.filter(
      (eleve) =>
        eleve.nom.toLowerCase().includes(query) ||
        eleve.promotion.toLowerCase().includes(query)
    );
  }

  return filteredEleves;
});
</script>

<style scoped src="./ReferentAcceuil.css"></style>
  