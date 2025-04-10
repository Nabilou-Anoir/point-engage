<template>
  <div class="main-layout">
    <main class="main-content">
      <!-- 🎛️ Filtres -->
      <!-- (tes filtres inchangés ici) -->

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
                <th>Validé</th>
                <th>Remarque</th>
                <th>Envoyer</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(eleve, index) in elevesFiltres" :key="index">
                <td @click="openModal(eleve)" class="clickable">{{ eleve.nom }}</td>
                <td>{{ eleve.promotion }}</td>
                <td>{{ eleve.typeEngagement }}</td>
                <td>
                  <span @click="toggleFicheVisible(eleve)">
                    {{ eleve.ficheVisible ? "👁️" : "👁️‍🗨️" }}
                  </span>
                </td>
                <td><input type="checkbox" v-model="eleve.valide" /></td>
                <td><input type="text" v-model="eleve.remarqueReferent" /></td>
                <td>
                  <input
                    type="checkbox"
                    v-model="eleve.envoye"
                    @change="validerEnvoi(eleve)"
                    :disabled="eleve.envoye"
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

      <!-- ✅ MODALE -->
      <div v-if="selectedEleve" class="modal-overlay" @click.self="closeModal">
        <div class="modal">
          <h2>Détails de {{ selectedEleve.nom }}</h2>
          <p><strong>Promotion :</strong> {{ selectedEleve.promotion }}</p>
          <p><strong>Type d'engagement :</strong> {{ selectedEleve.typeEngagement }}</p>
          <p><strong>Remarque :</strong> {{ selectedEleve.remarqueReferent }}</p>
          <p><strong>Statut :</strong> {{ selectedEleve.valide ? 'Validé' : 'Non validé' }}</p>
          <button @click="closeModal">Fermer</button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import axios from "axios";

const baseURL = "http://localhost:8989/api";
const referentId = localStorage.getItem("referentId") || 1;

const eleves = ref([]);
const searchQuery = ref("");
const selectedSemestre = ref("1");
const selectedAnnee = ref("2024/2025");
const dateDebut = ref("");
const dateFin = ref("");

const selectedEleve = ref(null);

const toggleFicheVisible = (eleve) => {
  eleve.ficheVisible = !eleve.ficheVisible;
};

const openModal = (eleve) => {
  selectedEleve.value = eleve;
};

const closeModal = () => {
  selectedEleve.value = null;
};

const fetchParticipations = async () => {
  try {
    const { data: participations } = await axios.get(`${baseURL}/participes/referent/${referentId}`);
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
        console.error("⛔ Erreur chargement élève :", err);
        return null;
      }
    }));

    eleves.value = eleveList.filter(Boolean);
  } catch (error) {
    console.error("❌ Erreur chargement participations :", error);
  }
};

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
    eleve.envoye = false;
  }
};

onMounted(fetchParticipations);
</script>

<style scoped>
.clickable {
  cursor: pointer;
  color: #6a3fa0;
  font-weight: bold;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  max-width: 400px;
  width: 90%;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
}

.modal button {
  margin-top: 20px;
  background-color: #6a3fa0;
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.modal button:hover {
  background-color: #542b83;
}
</style>
