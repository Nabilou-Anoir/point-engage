<template>
  <div class="referent-view">
    <!-- Barre de recherche et filtre -->
    <div class="search-and-filter">
      <input v-model="searchQuery" placeholder="Rechercher un élève..." class="search-input" />

      <select v-model="selectedFilter" class="filter-select">
        <option value="">Tous les types d'engagement</option>
        <option value="Climat-Environnement">Climat-Environnement</option>
        <option value="Ecole">Ecole</option>
        <option value="Diversité">Diversité</option>
      </select>

      <select v-model="selectedPromotion" class="filter-select">
        <option value="">Toutes les promotions</option>
        <option v-for="promotion in availablePromotions" :key="promotion" :value="promotion">
          {{ promotion }}
        </option>
      </select>
    </div>

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
          <tr v-for="(student, index) in paginatedStudents" :key="index">
            <td @click="openStudentModal(student)">
              {{ student.name }}
              <i class="fas fa-envelope message-icon" @click.stop="sendEmail(student)"></i>
            </td>
            <td class="promotion-cell">{{ student.promotion }}</td>
            <td class="engagement-cell">{{ student.engagementType }}</td>
            <td class="summary-cell">{{ student.summary }}</td>
            <td>
              <button @click="openChooseModal(student)">Choisir</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="pagination">
      <button @click="previousPage" :disabled="currentPage === 1" class="pagination-button prev-next">
        <i class="fas fa-chevron-left"></i>
      </button>
      <div class="page-numbers">
        <button
          v-for="page in totalPages"
          :key="page"
          @click="goToPage(page)"
          :class="{ active: currentPage === page }"
          class="page-number"
        >
          {{ page }}
        </button>
      </div>
      <button @click="nextPage" :disabled="currentPage === totalPages" class="pagination-button prev-next">
        <i class="fas fa-chevron-right"></i>
      </button>
    </div>

    <div v-if="isModalOpen" class="modal-overlay">
      <div class="modal-content">
        <span class="close-modal" @click="closeModal">&times;</span>
        <div class="modal-header">
          <h2>{{ selectedStudent.name }}</h2>
        </div>
        <div class="modal-body">
          <div class="student-info">
            <p class="center-date">2024/2025</p>
            <p class="center-bold">{{ selectedStudent.semester }}</p>
          </div>
          <div class="student-details">
            <div class="detail-item">
              <span class="detail-label">Promotion:</span>
              <span class="detail-value">{{ selectedStudent.promotion }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Type d'engagement:</span>
              <span class="detail-value">{{ selectedStudent.engagementType }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Type d'action:</span>
              <span class="detail-value">{{ selectedStudent.actionType }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Période de réalisation:</span>
              <span class="detail-value">{{ selectedStudent.period }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Description:</span>
              <span class="detail-value">{{ selectedStudent.description }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Points:</span>
              <span class="detail-value">{{ selectedStudent.totalPoints }}</span>
            </div>
            <div class="detail-item">
              <span class="detail-label">Statut:</span>
              <span class="detail-value">{{ selectedStudent.statut ? '✅ Validée' : '⏳ En attente' }}</span>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="modal-button" @click="closeModal">Fermer</button>
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
      referentEmail: '',
      referentName: '',
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
      const end = start + this.itemsPerPage;
      return this.filteredStudents.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.filteredStudents.length / this.itemsPerPage);
    },
  },
  async mounted() {
    try {
      const participationsRes = await axios.get('http://localhost:8989/api/participes?page=0&size=100');
      const participations = participationsRes.data._embedded?.participes || [];

      console.log("✅ Participations récupérées:", participations);

      const pending = participations.filter(p => p.statut === false || p.statut === null);
      console.log("📌 Participations en attente:", pending);

      const studentList = await Promise.all(pending.map(async (p) => {
        const [etudiant, action, semestre] = await Promise.all([
          axios.get(p._links.etudiant.href),
          axios.get(p._links.action.href),
          axios.get(p._links.semestre.href)
        ]);

        return {
          name: etudiant.data.prenom + ' ' + etudiant.data.nom,
          email: etudiant.data.email,
          promotion: etudiant.data.promotion,
          engagementType: action.data.referentiel?.nom || 'N/A',
          actionType: action.data.nom,
          summary: p.descriptionParticipation,
          period: this.formatDate(semestre.data.dateDebutSemestre) + ' - ' + this.formatDate(semestre.data.dateFinSemestre),
          description: p.descriptionParticipation,
          actionId: action.data.id,
          etudiantId: etudiant.data.idEtudiant,
          semestreId: semestre.data.idSemestre,
        };
      }));

      console.log("📚 Liste finale des étudiants:", studentList);

      this.students = studentList;
      this.availablePromotions = [...new Set(studentList.map(s => s.promotion))];
    } catch (error) {
      console.error('❌ Erreur de chargement des participations :', error);
    }
  },
  methods: {
    sendEmail(student) {
      if (student.email) {
        window.location.href = `mailto:${student.email}`;
      } else {
        alert("Aucune adresse e-mail trouvée pour cet étudiant.");
      }
    },
    async openStudentModal(student) {
      try {
        const res = await axios.get(
          `http://localhost:8989/api/participes/${student.etudiantId}/${student.actionId}/${student.semestreId}`
        );

        const participation = res.data;

        console.log("🔍 Détails de la participation récupérés:", participation);

        this.selectedStudent = {
          ...student,
          description: participation.descriptionParticipation,
          summary: participation.descriptionParticipation,
          statut: participation.statut,
        };

        this.isModalOpen = true;
      } catch (error) {
        console.error("❌ Erreur en récupérant les détails de la participation :", error);
        alert("❌ Impossible de récupérer les détails de la fiche.");
      }
    },
    closeModal() {
      this.isModalOpen = false;
    },
    openChooseModal(student) {
      this.selectedStudent = student;
      this.isChooseModalOpen = true;
    },
    closeChooseModal() {
      this.isChooseModalOpen = false;
    },
    async confirmReferent() {
      if (this.referentEmail && this.referentName) {
        try {
          await axios.put(`http://localhost:8989/api/actions/${this.selectedStudent.actionId}/referent`, {
            email: this.referentEmail,
            nom: this.referentName
          });

          alert(`✅ Référent ${this.referentName} a bien été associé à l’action !`);
          this.closeChooseModal();
        } catch (error) {
          console.error("❌ Erreur lors de l’envoi au référent :", error);
          alert("❌ Une erreur est survenue, veuillez réessayer.");
        }
      } else {
        alert('⚠️ Veuillez remplir tous les champs.');
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
    formatDate(dateStr) {
      const d = new Date(dateStr);
      return d.toLocaleDateString('fr-FR', { year: 'numeric', month: 'short' });
    }
  },
};
</script>

<style scoped src="./ReferentView.css"></style>