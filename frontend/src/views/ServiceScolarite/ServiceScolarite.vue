<!-- http://localhost:5174/scolarite/accueil -->

<template>
  <div class="main-layout">
    <!-- Sidebar -->
    <aside class="sidebar">
      <div class="filter-options">
        <div v-for="(annee, index) in anneesUniversitaires" :key="index" class="annee-group">
          <h4 @click="toggleSemestre(index)" :class="{ expanded: annee.showSemestres }">
            {{ annee.annee }}
          </h4>
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
    </aside>

    <!-- Main Content -->
    <main class="main-content">
      
      <div class="top-filter">
      <!-- Filtres à gauche -->
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

      <!-- Barre de recherche et bouton Télécharger à droite -->
      <div class="right-group">
        <div class="search-container">
          <input
            type="text"
            v-model="searchQuery"
            placeholder="Rechercher..."
            class="search-input"
          />
        </div>
        <button class="download-btn" @click="genererPDF">
          <span class="download-icon">📄</span> Télécharger
        </button>
      </div>
      </div>
      <div class="filter-group date-range">
        <div class="date-container">
          <label class="selectlabel">Année universitaire</label>
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
          <label class="selectlabel">Année universitaire</label>
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

      <h1 class="title">Service Scolarité</h1>

      <div class="table-section">
        <div class="table-container">
          <table class="styled-table">
            <thead>
              <tr>
                <th>Nom</th>
                <th>Promotion</th>
                <th class="align-right">Points accordés</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(eleve, index) in elevesFiltres" :key="index">
                <td>{{ eleve.nom }}</td>
                <td>{{ eleve.promotion }}</td>
                <td class="align-right">
                  <input type="number" v-model="eleve.pointsAccordes" class="editable-input" />
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
import { ELEVES } from "../../constante/HistoriqueViewConstants";
import jsPDF from "jspdf"; // Importer jsPDF

// Données des élèves
const eleves = ref(ELEVES);
const searchQuery = ref("");

// Données pour les années universitaires et les semestres
const anneesUniversitaires = ref([
  { annee: "2024/2025", showSemestres: true },
  { annee: "2023/2024", showSemestres: true },
  { annee: "2022/2023", showSemestres: true },
]);

const selectedSemestre = ref("S1"); // Semestre sélectionné
const selectedAnnee = ref("2024/2025"); // Année universitaire sélectionnée
const dateDebut = ref(""); // Date de début
const dateFin = ref(""); // Date de fin

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
};

// Fonction pour réinitialiser les filtres
const clearFilters = () => {
  searchQuery.value = "";
  selectedSemestre.value = {};
  dateDebut.value = "";
  dateFin.value = "";
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
      return true; // Remplacez par votre logique de filtrage
    });
  }

  // Filtrage par date de début et de fin
  if (dateDebut.value && dateFin.value) {
    filteredEleves = filteredEleves.filter((eleve) => {
      const eleveDate = new Date(eleve.date); // Remplacez `eleve.date` par le champ de date de l'élève
      return eleveDate >= new Date(dateDebut.value) && eleveDate <= new Date(dateFin.value);
    });
  }

  return filteredEleves;
});

// Fonction pour générer un PDF
const genererPDF = () => {
  const doc = new jsPDF();

  // Titre du document
  doc.setFontSize(18);
  doc.text("Historique des élèves", 10, 10);

  // En-têtes du tableau
  const headers = ["Nom", "Promotion", "Points accordés"];

  // Données du tableau
  const data = elevesFiltres.value.map((eleve) => [
    eleve.nom,
    eleve.promotion,
    eleve.pointsAccordes,
  ]);

  // Générer le tableau dans le PDF
  doc.autoTable({
    head: [headers],
    body: data,
    startY: 20, // Position verticale de départ
  });

  // Sauvegarder le PDF
  doc.save("historique_eleves.pdf");
};
</script>
<style scoped src="./ServiceScolarite.css"></style>