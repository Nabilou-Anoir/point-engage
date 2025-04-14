<template>
  <div class="referent-view">
    <!-- Filtres -->
    <div class="search-and-filter">
      <input type="text" v-model="searchQuery" placeholder="Rechercher un élève..." class="search-input" />
    </div>

    <!-- Tableau -->
    <div class="table-container">
      <table class="students-table">
        <thead>
        <tr>
          <th>Nom</th>
          <th>Promotion</th>
          <th>Type d'engagement</th>
          <th>Résumé</th>
          <th>Envoie au référent</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="student in paginatedStudents" :key="student.etudiantId">
          <td @click="openStudentModal(student)" style="cursor: pointer; color: blue">{{ student.name }}</td>
          <td>{{ student.promotion }}</td>
          <td>{{ student.engagementType }}</td>
          <td>{{ student.summary }}</td>
          <td><button @click="openChooseModal(student)">Assigner</button></td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="pagination">
      <button @click="previousPage" :disabled="currentPage === 1">&lt;</button>
      <button v-for="page in totalPages" :key="page" @click="goToPage(page)" :class="{ active: currentPage === page }">{{ page }}</button>
      <button @click="nextPage" :disabled="currentPage === totalPages">&gt;</button>
    </div>

    <!-- Modale Détail -->
    <div v-if="isModalOpen" class="modal-overlay">
      <div class="modal-content">
        <span class="close-modal" @click="closeModal">&times;</span>
        <div class="modal-header">
          <h2>{{ selectedStudent.name }}</h2>
        </div>
        <div class="modal-body">
          <p><strong>Promotion:</strong> {{ selectedStudent.promotion }}</p>
          <p><strong>Engagement:</strong> {{ selectedStudent.engagementType }}</p>
          <p><strong>Action:</strong> {{ selectedStudent.actionType }}</p>
          <p><strong>Description:</strong> {{ selectedStudent.description }}</p>
        </div>
        <div class="modal-footer">
          <button @click="closeModal">Fermer</button>
        </div>
      </div>
    </div>

    <!-- Modale Choix Référent -->
    <div v-if="isChooseModalOpen" class="modal-overlay">
      <div class="modal-content">
        <span class="close-modal" @click="closeChooseModal">&times;</span>
        <div class="modal-header">
          <h2>Assigner un Référent</h2>
        </div>
        <div class="modal-body">
          <label>Sélectionnez un référent :</label>
          <select v-model="selectedReferentId">
            <option disabled value="">-- Choisir un référent --</option>
            <option v-for="ref in referents" :key="ref.idReferent" :value="ref.idReferent">
              {{ ref.nom }} ({{ ref.email }})
            </option>
          </select>
        </div>
        <div class="modal-footer">
          <button @click="confirmReferent">Confirmer</button>
          <button @click="closeChooseModal">Annuler</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      students: [],
      searchQuery: '',
      selectedFilter: '',
      selectedPromotion: '',
      availablePromotions: [],
      currentPage: 1,
      itemsPerPage: 5,
      isModalOpen: false,
      isChooseModalOpen: false,
      selectedStudent: null,
      referents: [],
      selectedReferentId: '',
    };
  },
  computed: {
    filteredStudents() {
      return this.students.filter((student) => {
        const matchesSearch = student.name.toLowerCase().includes(this.searchQuery.toLowerCase());
        const matchesFilter = this.selectedFilter ? student.engagementType === this.selectedFilter : true;
        const matchesPromotion = this.selectedPromotion ? student.promotion === this.selectedPromotion : true;
        return matchesSearch && matchesFilter && matchesPromotion;
      });
    },
    paginatedStudents() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      return this.filteredStudents.slice(start, start + this.itemsPerPage);
    },
    totalPages() {
      return Math.ceil(this.filteredStudents.length / this.itemsPerPage);
    },
  },
  async mounted() {
    try {
      const participationsRes = await axios.get('http://localhost:8989/api/participes');
      const participations = participationsRes.data.filter(p => p.pointAction == null);

      const studentList = await Promise.all(participations.map(async (p) => {
        try {
          const [etudiant, action, semestre] = await Promise.all([
            axios.get(`http://localhost:8989/api/etudiants/${p.id.idEtudiant}`),
            axios.get(`http://localhost:8989/api/actions/${p.id.idAction}`),
            axios.get(`http://localhost:8989/api/semestres/${p.id.idSemestre}`)
          ]);

          let referentielNom = 'Non défini';
          if (p.idReferentiel) {
            try {
              const ref = await axios.get(`http://localhost:8989/api/referentiels/${p.idReferentiel}`);
              referentielNom = ref.data.nom || 'Non défini';
            } catch (err) {}
          }

          return {
            name: etudiant.data.prenom + ' ' + etudiant.data.nom,
            email: etudiant.data.email,
            promotion: etudiant.data.promotion,
            engagementType: referentielNom,
            actionType: action.data.nom,
            summary: p.resumeDirecteur || '-',
            description: p.descriptionParticipation,
            actionId: action.data.idAction,
            etudiantId: etudiant.data.idEtudiant,
            semestreId: semestre.data.idSemestre,
          };
        } catch (e) {
          return null;
        }
      }));

      this.students = studentList.filter(Boolean);
      this.availablePromotions = [...new Set(this.students.map(s => s.promotion))];

      const refRes = await axios.get('http://localhost:8989/api/referents');
      this.referents = refRes.data;
    } catch (error) {
      console.error('Erreur chargement données :', error);
    }
  },
  methods: {
    openStudentModal(student) {
      this.selectedStudent = student;
      this.isModalOpen = true;
    },
    closeModal() {
      this.isModalOpen = false;
    },
    openChooseModal(student) {
      this.selectedStudent = student;
      this.selectedReferentId = '';
      this.isChooseModalOpen = true;
    },
    closeChooseModal() {
      this.isChooseModalOpen = false;
    },
    async confirmReferent() {
      if (!this.selectedReferentId) {
        alert('Veuillez sélectionner un référent.');
        return;
      }

      try {
        await axios.put(`http://localhost:8989/api/participes/${this.selectedStudent.etudiantId}/${this.selectedStudent.actionId}/${this.selectedStudent.semestreId}/referent`, {
          idReferent: this.selectedReferentId,
        });

        alert('Référent associé avec succès !');
        this.closeChooseModal();
      } catch (error) {
        alert("Erreur lors de l'association.");
      }
    },
    previousPage() {
      if (this.currentPage > 1) this.currentPage--;
    },
    nextPage() {
      if (this.currentPage < this.totalPages) this.currentPage++;
    },
    goToPage(page) {
      this.currentPage = page;
    },
  },
};
</script>

<style scoped src="./ReferentView.css"></style>
