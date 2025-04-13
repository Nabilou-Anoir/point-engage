<template>
  <div class="main-content">
    <h2 class="title">Attribuer des points</h2>

    <!-- Conteneur du tableau permettant le défilement horizontal -->
    <div class="table-container">
      <table class="styled-table">
        <thead>
        <tr>
          <th>Nom</th>
          <th>Action</th>
          <th>Engagement</th>
          <th>Résumé</th>
          <th>Remarque du Référent</th>
          <th>Validé</th>
          <th>Points</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="student in students"
            :key="student.etudiantId + '-' + student.originalIdAction + '-' + student.semestreId">
          <td>
            <a href="#" @click.prevent="openStudentModal(student)">{{ student.name }}</a>
          </td>
          <!-- Sélection d'une action -->
          <td>
            <select v-model="student.idAction"
                    class="editable-input select-input"
                    @change="replaceParticipation(student)">
              <option v-for="a in allActions"
                      :key="a.idAction"
                      :value="a.idAction">
                {{ a.nom }}
              </option>
            </select>
          </td>
          <!-- Sélection d'un référentiel (engagement) -->
          <td>
            <select v-model="student.idReferentiel"
                    class="editable-input select-input"
                    @change="replaceParticipation(student)">
              <option v-for="r in allReferentiels"
                      :key="r.idReferentiel"
                      :value="r.idReferentiel">
                {{ r.nom }}
              </option>
            </select>
          </td>
          <!-- Résumé modifiable (lié à resumeDirecteur) -->
          <td>
            <input type="text"
                   v-model="student.resumeDirecteur"
                   class="editable-input" />
          </td>
          <!-- Remarque du Référent (lecture seule) -->
          <td>
            {{ student.remarqueReferent }}
          </td>
          <!-- Checkbox personnalisé pour "Validé" -->
          <td>
            <label class="custom-checkbox">
              <input type="checkbox"
                     v-model="student.statut"
                     class="hidden-checkbox" />
              <span class="checkmark"></span>
            </label>
          </td>
          <!-- Points liés à pointAction -->
          <td>
            <input type="number"
                   min="0"
                   step="0.01"
                   v-model.number="student.pointAction"
                   class="editable-input"
                   @input="limiterValeur(student, 'pointAction')" />
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Bouton d'enregistrement aligné à droite -->
    <div class="button-container">
      <button class="btn-submit" @click="validerEnvoi">
        Enregistrer
      </button>
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
          <div class="detail-row">
            <div class="detail-label">Date de soumission:</div>
            <div class="detail-value">
              {{ selectedStudent.dateSoumission || 'Non disponible' }}
            </div>
          </div>
        </div>
        <button class="modal-btn" @click="showModal = false">Fermer</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      allStudents: [], // Liste complète des participations récupérées depuis l'API
      students: [],    // Liste affichée : uniquement les étudiants dont pointAction est null
      allActions: [],
      allReferentiels: [],
      baseURL: 'http://localhost:8989/api',
      showModal: false,
      selectedStudent: null
    };
  },
  async mounted() {
    await this.loadReferentiels();
    await this.loadActions();
    await this.loadStudents();
  },
  methods: {
    async loadStudents() {
      try {
        const res = await axios.get(`${this.baseURL}/participes`);
        const participations = res.data;
        this.allStudents = await Promise.all(
          participations.map(async (p) => {
            const etu = await axios.get(`${this.baseURL}/etudiants/${p.id.idEtudiant}`);
            return {
              name: `${etu.data.prenom} ${etu.data.nom}`,
              idAction: p.id.idAction,
              originalIdAction: p.id.idAction,
              etudiantId: p.id.idEtudiant,
              semestreId: p.id.idSemestre,
              idReferentiel: p.idReferentiel,
              originalIdReferentiel: p.idReferentiel,
              pointAction: p.pointAction === null ? null : parseFloat(p.pointAction),
              statut: p.statut,
              resumeDirecteur: p.resumeDirecteur || '',
              remarqueReferent: p.remarqueReferent || '',
              descriptionParticipation: p.descriptionParticipation || '',
              dateSoumission: p.dateSoumission
            };
          })
        );

        // Filtrer pour n'afficher que les étudiants dont pointAction est null
        this.students = this.allStudents.filter(student => student.pointAction === null);

        // Mettre à jour la liste des IDs à afficher dans le sessionStorage
        const studentIdsToShow = this.students.map(student => student.etudiantId);
        sessionStorage.setItem("studentsToShow", JSON.stringify(studentIdsToShow));
      } catch (error) {
        console.error("Erreur lors du chargement des étudiants :", error);
      }
    },
    async loadReferentiels() {
      try {
        const res = await axios.get(`${this.baseURL}/referentiels`);
        this.allReferentiels = res.data;
      } catch (error) {
        console.error("Erreur lors du chargement des référentiels :", error);
      }
    },
    async loadActions() {
      try {
        const res = await axios.get(`${this.baseURL}/actions`);
        this.allActions = res.data;
      } catch (error) {
        console.error("Erreur lors du chargement des actions :", error);
      }
    },
    getActionName(idAction) {
      const action = this.allActions.find(a => a.idAction === idAction);
      return action ? action.nom : 'Action inconnue';
    },
    limiterValeur(student, champ) {
      const value = parseFloat(student[champ] || 0);
      if (value > 0.5) {
        alert(`Le champ ${champ} ne peut pas dépasser 0.50`);
        student[champ] = 0.5;
      } else {
        student[champ] = parseFloat(value.toFixed(2));
      }
    },
    async replaceParticipation(student) {
      if (student.idAction === student.originalIdAction &&
        student.idReferentiel === student.originalIdReferentiel) {
        return;
      }
      try {
        const oldKey = `${this.baseURL}/participes/${student.etudiantId}/${student.originalIdAction}/${student.semestreId}`;
        const current = await axios.get(oldKey);
        const currentData = current.data;
        const payload = {
          id: {
            idEtudiant: student.etudiantId,
            idAction: student.idAction,
            idSemestre: student.semestreId,
          },
          etudiant: { idEtudiant: student.etudiantId },
          action: { idAction: student.idAction },
          semestre: { idSemestre: student.semestreId },
          idReferentiel: student.idReferentiel,
          pointAction: student.pointAction,
          statut: student.statut,
          resumeDirecteur: student.resumeDirecteur,
          descriptionParticipation: currentData.descriptionParticipation || '',
          dateSoumission: currentData.dateSoumission || null,
        };
        await axios.delete(oldKey);
        await axios.post(`${this.baseURL}/participes`, payload);
        student.originalIdAction = student.idAction;
        student.originalIdReferentiel = student.idReferentiel;
        alert("Participation mise à jour avec succès");
      } catch (error) {
        console.error("Erreur lors du remplacement de la participation :", error);
        alert("Erreur lors du remplacement de la participation");
        student.idAction = student.originalIdAction;
        student.idReferentiel = student.originalIdReferentiel;
      }
    },
    async validerEnvoi() {
      try {
        await Promise.all(
          this.students.map((student) => {
            if (parseFloat(student.pointAction) > 0.5) {
              alert(`La valeur de pointAction pour ${student.name} dépasse 0.50 — modification ignorée.`);
              return Promise.resolve();
            }
            const payload = {
              id: {
                idEtudiant: student.etudiantId,
                idAction: student.idAction,
                idSemestre: student.semestreId,
              },
              etudiant: { idEtudiant: student.etudiantId },
              action: { idAction: student.idAction },
              semestre: { idSemestre: student.semestreId },
              idReferentiel: student.idReferentiel,
              pointAction: student.pointAction,
              statut: student.statut,
              resumeDirecteur: student.resumeDirecteur,
              remarqueReferent: student.remarqueReferent,
              descriptionParticipation: student.descriptionParticipation || '',
              dateSoumission: student.dateSoumission || null,
            };
            return axios.post(`${this.baseURL}/participes`, payload);
          })
        );
        // Récupérer tous les IDs d'étudiants qui ont été traités (y compris les nouveaux)
        const allStudentIds = this.allStudents.map(student => student.etudiantId);

        // Stocker cette liste complète pour PointCumuler
        sessionStorage.setItem("allProcessedStudents", JSON.stringify(allStudentIds));

        // Signaler la mise à jour pour que la vue PointCumuler prenne en compte les modifications
        sessionStorage.setItem("dataUpdated", "true");

        // Recharger la liste après la mise à jour
        await this.loadStudents();
        alert("Mise à jour réussie !");
      } catch (error) {
        console.error("Erreur lors de l'enregistrement :", error);
        alert("Échec de la mise à jour. Vérifiez les données et réessayez.");
      }
    },
    openStudentModal(student) {
      this.selectedStudent = student;
      this.showModal = true;
    }
  }
};
</script>

<style scoped>
/* Contenu principal */
.main-content {
  font-family: 'Inter', sans-serif;
  max-width: 100%;
  margin: 0 auto;
  background-color: #f8f9fa;
  padding: 20px;
}

/* Titre */
.title {
  font-size: 24px;
  font-weight: 600;
  color: #6a3fa0;
  margin-bottom: 20px;
  text-align: start;
}

/* Conteneur du tableau pour défilement horizontal */
.table-container {
  width: 100%;
  overflow-x: auto;
  background: #ffffff;
  border-radius: 12px;
  margin-bottom: 20px;
}

/* Tableau */
.styled-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: auto;
}

.styled-table th,
.styled-table td {
  padding: 14px;
  text-align: left;
  border-bottom: 1px solid #eee;
  word-wrap: break-word;
}

.styled-table th {
  background: #f8f8f8;
  font-weight: 600;
  color: #555;
}

.styled-table tbody tr:hover {
  background: #f5f5f5;
}

/* Champs modifiables */
.editable-input {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  width: 100%;
  box-sizing: border-box;
  transition: border-color 0.3s ease;
}

.editable-input:focus {
  border-color: #6a3fa0;
  outline: none;
}

/* Inputs de type select */
.select-input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  transition: border-color 0.3s ease;
}

.select-input:focus {
  border-color: #6a3fa0;
  outline: none;
}

/* Checkbox personnalisé */
.custom-checkbox {
  display: inline-block;
  position: relative;
  cursor: pointer;
  width: 20px;
  height: 20px;
}

.hidden-checkbox {
  opacity: 0;
  position: absolute;
}

.checkmark {
  position: absolute;
  top: 0;
  left: 0;
  width: 20px;
  height: 20px;
  background-color: #fff;
  border: 2px solid #6a3fa0;
  border-radius: 4px;
  transition: background-color 0.3s ease;
}

.hidden-checkbox:checked ~ .checkmark {
  background-color: #6a3fa0;
}

.checkmark::after {
  content: "";
  position: absolute;
  display: none;
  left: 6px;
  top: 2px;
  width: 5px;
  height: 10px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.hidden-checkbox:checked ~ .checkmark::after {
  display: block;
}

/* Bouton d'enregistrement */
.button-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.btn-submit {
  display: block;
  padding: 16px;
  background: #6a3fa0;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  margin-top: 20px;
  font-weight: 600;
  transition: background-color 0.3s ease;
}

.btn-submit:hover {
  background: #5a2f90;
}

/* Modal agrandi */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  background: white;
  padding: 40px;             /* Augmentation du padding */
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.3);
  width: 600px;              /* Augmentation de la largeur */
  max-width: 90%;
  margin: 0 20px;
}

.modal h3 {
  font-size: 24px;           /* Taille augmentée du titre */
  color: #333;
  margin-bottom: 20px;
}

.modal-btn {
  padding: 12px 24px;        /* Padding agrandi */
  background: #6a3fa0;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.modal-btn:hover {
  background: #5a2f90;
}

/* Détails de la modal */
.detail-row {
  margin-bottom: 15px;
}

.detail-label {
  font-weight: bold;
  margin-bottom: 5px;
}

.detail-value {
  padding: 5px 0;
}

/* Responsive */
@media (max-width: 768px) {
  .btn-submit {
    font-size: 14px;
    padding: 12px;
  }
  .modal {
    width: 90%;
    padding: 30px;
  }
  .modal h3 {
    font-size: 20px;
  }
  .modal-btn {
    font-size: 14px;
    padding: 10px 20px;
  }
}
</style>
