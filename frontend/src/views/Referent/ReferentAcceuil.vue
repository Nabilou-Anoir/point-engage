<template>
  <div class="main-layout">
    <main class="main-content">
      <!-- 🎛️ Filtres -->
      <div class="top-filter">
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
                <td>
                  <input
                    type="text"
                    v-model="eleve.remarqueReferent"
                    class="text-input"
                  />
                </td>
                <td>
                  <input
                    type="checkbox"
                    v-model="eleve.envoye"
                    @change="validerEnvoi(eleve)"
                    :disabled="eleve.envoye"
                    class="styled-checkbox"
                  />
                </td>
              </tr>
              <tr v-if="elevesFiltres.length === 0">
                <td colspan="7">Aucune participation trouvée.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
          <!-- Modal d'affichage des détails de l'étudiant -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal">
        <h3>Détails de l'étudiant</h3>
        <div class="modal-body" v-if="selectedStudent">
          <div class="detail-row">
            <div class="detail-label">Nom:</div>
            <div class="detail-value">{{ selectedStudent.name }}</div>
          </div>
          <div class="detail-row">
            <div class="detail-label">Action:</div>
            <div class="detail-value">{{ getActionName(selectedStudent.idAction) }}</div>
          </div>
          <div class="detail-row">
            <div class="detail-label">Description de la participation:</div>
            <div class="detail-value">{{ selectedStudent.descriptionParticipation }}</div>
          </div>
        </div>
        <button class="modal-btn" @click="showModal = false">Fermer</button>
      </div>
    </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import axios from "axios";

const baseURL = "http://localhost:8989/api";

// 🔐 Référent connecté (à remplacer par une vraie auth ou localStorage)
const referentId = localStorage.getItem("referentId") || 1;

const eleves = ref([]);
const searchQuery = ref("");
const selectedSemestre = ref("1");
const selectedAnnee = ref("2024/2025");
const dateDebut = ref("");
const dateFin = ref("");

const toggleFicheVisible = (eleve) => {
  eleve.ficheVisible = !eleve.ficheVisible;
};

const fetchParticipations = async () => {
  try {
    const { data: participations } = await axios.get(`${baseURL}/participes/referent/${referentId}`);
    console.log("📡 Participations du référent :", participations);

    const eleveList = await Promise.all(participations.map(async (p) => {
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
          etudiantId: p.id.idEtudiant,
          actionId: p.id.idAction,
          semestreId: p.id.idSemestre,
        };
      } catch (err) {
        console.error(" Erreur chargement élève :", err);
        return null;
      }
    }));

    eleves.value = eleveList.filter(Boolean);
    console.log(" Élèves à afficher :", eleves.value);
  } catch (error) {
    console.error(" Erreur chargement participations :", error);
  }
};

// 🔍 À adapter avec filtres si besoin
const elevesFiltres = computed(() => eleves.value);

const validerEnvoi = async (eleve) => {
  if (!eleve.envoye) {
    alert("Tu ne peux pas désenvoyer une participation.");
    eleve.envoye = true;
    return;
  }

  try {
    await axios.put(`${baseURL}/participes/${eleve.etudiantId}/${eleve.actionId}/${eleve.semestreId}`, {
      statut: eleve.valide,
      remarqueReferent: eleve.remarqueReferent,
    });
    console.log("✅ Participation envoyée pour :", eleve.nom);
  } catch (e) {
    console.error("❌ Erreur d’envoi :", e.response?.data || e.message);
    alert("Erreur lors de l’envoi.");
    eleve.envoye = false; // rollback si échec
  }
};
openStudentModal(student) {
      this.selectedStudent = student;
      this.showModal = true;
    }

onMounted(fetchParticipations);
</script>

<style scoped src="./ReferentAcceuil.css"></style>
