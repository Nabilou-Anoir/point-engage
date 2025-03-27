<template>
  <div class="main-layout">
    <!-- Sidebar -->
    <div class="sidebar">
      <div class="filter-options">
        <div v-for="(annee, index) in anneesUniversitaires" :key="index" class="annee-group">
          <h4 @click="toggleSemestre(index)">{{ annee.annee }}</h4>
          <div v-if="annee.showSemestres" class="semestre-group">
            <button
              class="semestre-btn"
              @click="filtrerParSemestre(annee.annee, 'S1')"
              :class="{ active: isSemestreActive(annee.annee, 'S1') }"
            >
              Semestre 1
            </button>
            <button
              class="semestre-btn"
              @click="filtrerParSemestre(annee.annee, 'S2')"
              :class="{ active: isSemestreActive(annee.annee, 'S2') }"
            >
              Semestre 2
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Contenu principal -->
    <div class="main-content">
      <!-- Filtre au-dessus de l'écran -->
      <div class="top-filter">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Rechercher par nom ou promotion..."
          class="search-input"
        />
        <button class="clear-filter-btn" @click="clearFilters">Afficher tous</button>
      </div>

      <!-- 📌 Titre -->
      <h1 class="title">Historique</h1>

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
            <tr v-for="(eleve, index) in elevesFiltres" :key="index">
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
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { ELEVES, validerEnvoi } from "../../constante/HistoriqueViewConstants";

const eleves = ref(ELEVES);
const dateDebut = ref("");
const dateFin = ref("");
const anneeUniversitaire = ref("2024/2025");
const showModal = ref(false);
const searchQuery = ref(""); // Pour le filtre de recherche

// Données pour les années universitaires et les semestres
const anneesUniversitaires = ref([
  { annee: "2024/2025", showSemestres: true },
  { annee: "2023/2024", showSemestres: true },
  { annee: "2022/2023", showSemestres: true },
]);

const selectedSemestre = ref({});

// Fonction pour basculer l'affichage des semestres
const toggleSemestre = (index) => {
  anneesUniversitaires.value[index].showSemestres = !anneesUniversitaires.value[index].showSemestres;
};

// Fonction pour vérifier si un semestre est actif
const isSemestreActive = (annee, semestre) => {
  return selectedSemestre.value[annee] === semestre;
};

// Fonction pour filtrer par année et semestre
const filtrerParSemestre = (annee, semestre) => {
  selectedSemestre.value[annee] = semestre;
  console.log(`Filtrer par année: ${annee}, semestre: ${semestre}`);
};

// Fonction pour réinitialiser les filtres
const clearFilters = () => {
  searchQuery.value = "";
  selectedSemestre.value = {};
};

// Propriété calculée pour filtrer les élèves
const elevesFiltres = computed(() => {
  let filteredEleves = eleves.value;

  // Filtrage par recherche
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase();
    filteredEleves = filteredEleves.filter(
      (eleve) =>
        eleve.nom.toLowerCase().includes(query) ||
        eleve.promotion.toLowerCase().includes(query)
    );
  }

  // Filtrage par année et semestre
  for (const [annee, semestre] of Object.entries(selectedSemestre.value)) {
    filteredEleves = filteredEleves.filter((eleve) => {
      // Implémentez la logique de filtrage par année et semestre ici
      // Par exemple, si vous avez une propriété `annee` et `semestre` dans chaque élève :
      // return eleve.annee === annee && eleve.semestre === semestre;
      return true; // Remplacez par votre logique de filtrage
    });
  }

  return filteredEleves;
});
</script>

<style scoped src="./HistoriqueView.css"></style>