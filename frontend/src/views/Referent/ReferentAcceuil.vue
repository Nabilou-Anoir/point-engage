<template>
  <div class="main-layout">
    <main class="main-content">
      <!-- 🎛️ Filtres -->
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

      <!-- 📅 Dates -->
      <div class="filter-group date-range">
        <div class="date-container">
          <label class="selectlabel">Date de début</label>
          <div class="select-container">
            <input type="date" v-model="dateDebut" class="select-input" />
          </div>
        </div>
        <div class="date-container">
          <label class="selectlabel">Date de fin</label>
          <div class="select-container">
            <input type="date" v-model="dateFin" class="select-input" />
          </div>
        </div>
      </div>

      <h1 class="title">Référent</h1>

      <!-- 🧾 Tableau -->
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
              <tr v-for="(eleve, index) in elevesFiltres" :key="index">
                <td>{{ eleve.nom }}</td>
                <td>{{ eleve.promotion }}</td>
                <td>{{ eleve.typeEngagement }}</td>
                <td>
                  <span @click="toggleFicheVisible(eleve)" class="eye-icon">
                    {{ eleve.ficheVisible ? "👁️" : "👁️‍🗨️" }}
                  </span>
                </td>
                <td>
                  <input type="checkbox" v-model="eleve.valide" class="styled-checkbox" />
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
import { ref, computed, onMounted } from "vue";
import axios from "axios";

const baseURL = "http://localhost:8989/api";

// 🔢 États
const eleves = ref([]);
const searchQuery = ref("");
const selectedSemestre = ref("S1");
const selectedAnnee = ref("2024/2025");
const dateDebut = ref("");
const dateFin = ref("");

// 👁️ Toggle affichage fiche
const toggleFicheVisible = (eleve) => {
  eleve.ficheVisible = !eleve.ficheVisible;
};

// ✅ Charger les données depuis le backend
const fetchParticipations = async () => {
  try {
    const { data: participations } = await axios.get(`${baseURL}/participes`);
    const filtered = participations.filter(p => p.statut === false || p.statut === null);

    const eleveList = await Promise.all(filtered.map(async (p) => {
      try {
        const [etudiant, action, referentiel] = await Promise.all([
          axios.get(`${baseURL}/etudiants/${p.id.idEtudiant}`),
          axios.get(`${baseURL}/actions/${p.id.idAction}`),
          axios.get(`${baseURL}/referentiels/${p.idReferentiel || 1}`),
        ]);

        return {
          nom: `${etudiant.data.prenom} ${etudiant.data.nom}`,
          promotion: etudiant.data.promotion,
          typeEngagement: referentiel.data.nom || "N/A",
          ficheVisible: false,
          remarqueReferent: p.remarqueReferent || "",
          valide: p.statut ?? false,
          envoye: false,
          etudiantId: etudiant.data.idEtudiant,
          actionId: action.data.idAction,
          semestreId: p.id.idSemestre,
        };
      } catch (err) {
        console.error("Erreur sur un élève :", err);
        return null;
      }
    }));

    eleves.value = eleveList.filter(Boolean);
  } catch (error) {
    console.error("Erreur lors du chargement :", error);
  }
};

// 📤 Envoi au backend
const validerEnvoi = async (eleve) => {
  try {
    await axios.put(`${baseURL}/participes/${eleve.etudiantId}/${eleve.actionId}/${eleve.semestreId}`, {
      statut: eleve.valide,
      remarqueReferent: eleve.remarqueReferent,
    });
    console.log("Participation mise à jour :", eleve.nom);
  } catch (e) {
    console.error("Erreur d’envoi :", e);
    alert("Erreur lors de l’envoi de la participation.");
  }
};

// 🔍 Filtrage dynamique
const elevesFiltres = computed(() => {
  return eleves.value.filter((eleve) => {
    const matchQuery = searchQuery.value
      ? eleve.nom.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        eleve.promotion.toLowerCase().includes(searchQuery.value.toLowerCase())
      : true;

    return matchQuery;
  });
});

// 📦 Appel initial
onMounted(fetchParticipations);
</script>

<style scoped src="./ReferentAcceuil.css"></style>
