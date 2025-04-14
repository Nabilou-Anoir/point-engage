<template>
  <div class="fiche-container">
    <!-- Header -->
    <header class="header">
      <div class="header-content">
        <h1>Fiche descriptive Ingénieur Engagé</h1>
        <p class="description">
          Remplissez ce formulaire pour décrire votre engagement en tant qu'ingénieur.
          Votre contribution est essentielle pour construire un avenir meilleur.
        </p>
      </div>
    </header>

    <!-- Affichage du nom de l'étudiant connecté (lecture seule) -->
    <div class="form-group" v-if="etudiant.idEtudiant">
      <label>Étudiant :</label>
      <input type="text" :value="etudiant.nom + ' ' + etudiant.prenom" disabled />
    </div>

    <!-- Formulaire -->
    <form @submit.prevent="submitForm" class="fiche-form">
      <!-- Sélection du semestre -->
      <div class="form-group">
        <label>Semestre <span class="required">*</span></label>
        <select v-model.number="fiche.semestre" required>
          <option disabled value="">-- Sélectionnez un semestre --</option>
          <option v-for="sem in semestres" :key="sem.idSemestre" :value="sem.idSemestre">
            S{{ sem.nbSemestre }}
          </option>
        </select>
        <p v-if="!isDepotOuvert" class="depot-ferme-msg">
          ❌ Le délai de dépôt est dépassé pour ce semestre.
        </p>
      </div>

      <!-- Sélection du référentiel -->
      <div class="form-group">
        <label>Référentiel <span class="required">*</span></label>
        <select v-model.number="fiche.referentiel" required>
          <option disabled value="">-- Sélectionnez un référentiel --</option>
          <option v-for="ref in referentiels" :key="ref.idReferentiel" :value="ref.idReferentiel">
            {{ ref.nom }}
          </option>
        </select>
      </div>

      <!-- Sélection de l'action -->
      <div class="form-group">
        <label>Action <span class="required">*</span></label>
        <select v-model.number="fiche.action" required>
          <option disabled value="">-- Sélectionnez une action --</option>
          <option v-for="act in actions" :key="act.idAction" :value="act.idAction">
            {{ act.nom }}
          </option>
        </select>
      </div>

      <!-- Dates de participation -->
      <div class="form-group">
        <label>Date de début de participation <span class="required">*</span></label>
        <input type="date" v-model="fiche.dateDebut" required />
      </div>
      <div class="form-group">
        <label>Date de fin de participation <span class="required">*</span></label>
        <input type="date" v-model="fiche.dateFin" required />
      </div>

      <!-- Description de la participation -->
      <div class="form-group">
        <label>Description de la participation <span class="required">*</span></label>
        <textarea rows="4" v-model="fiche.description" placeholder="Décrivez votre participation..." required></textarea>
      </div>

      <div class="form-actions">
        <button type="submit" class="btn-submit" :disabled="!isDepotOuvert">Envoyer</button>
        <button type="button" class="btn-cancel" @click="annuler">Annuler</button>
      </div>
    </form>

    <!-- Popup de confirmation -->
    <div v-if="showPopup" class="popup-overlay" @click.self="showPopup = false">
      <div class="popup-content">
        <div class="popup-icon">
          <svg v-if="submissionSuccess" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
          </svg>
          <svg v-else xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/>
          </svg>
        </div>
        <p class="popup-message">{{ popupMessage }}</p>
        <div class="popup-actions">
          <button class="btn-close" @click="showPopup = false">Fermer</button>
          <button v-if="submissionSuccess" class="btn-download" @click="downloadPDF">
            Télécharger le PDF
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import jsPDF from 'jspdf';
import logoUrl from '@/Images/logo-ISIS-vertical-RVB-HD.png';
import { imageToBase64 } from '@/utils/base.js';



export default {
  name: 'SaisirFicheView',
  data() {
    return {
      etudiant: { idEtudiant: null, nom: '', prenom: '' },
      fiche: { semestre: '', referentiel: '', action: '', dateDebut: '', dateFin: '', description: '' },
      semestres: [],
      referentiels: [],
      actions: [],
      showPopup: false,
      popupMessage: '',
      submissionSuccess: false,
      isDepotOuvert: true
    };
  },
  created() {
    const storedUser = sessionStorage.getItem('loggedInUser');
    if (storedUser) {
      const user = JSON.parse(storedUser);
      if (user.email) this.getEtudiantByEmail(user.email);
    }
    this.getSemestres();
    this.getReferentiels();
    this.getActions();
  },
  watch: {
    'fiche.semestre'(id) {
      const s = this.semestres.find(sem => sem.idSemestre === id);
      if (s && s.dateDebutDepot && s.dateFinDepot) {
        const now = new Date();
        this.isDepotOuvert = now >= new Date(s.dateDebutDepot) && now <= new Date(s.dateFinDepot);
      } else {
        this.isDepotOuvert = true;
      }
    }
  },
  methods: {
    getEtudiantByEmail(email) {
      const url = `http://localhost:8989/api/etudiants/byEmail?email=${encodeURIComponent(email)}`;
      fetch(url).then(res => res.json()).then(data => {
        if (data?.idEtudiant) {
          this.etudiant.idEtudiant = data.idEtudiant;
          this.etudiant.nom = data.nom;
          this.etudiant.prenom = data.prenom;
        }
      });
    },
    getSemestres() {
      fetch('http://localhost:8989/api/semestres')
        .then(res => res.json())
        .then(data => { this.semestres = data; });
    },
    getReferentiels() {
      fetch('http://localhost:8989/api/referentiels')
        .then(res => res.json())
        .then(data => { this.referentiels = data; });
    },
    getActions() {
      fetch('http://localhost:8989/api/actions')
        .then(res => res.json())
        .then(data => { this.actions = data; });
    },
    submitForm() {
      if (!this.isDepotOuvert) {
        this.popupMessage = "Le délai de dépôt est dépassé. Vous ne pouvez plus soumettre de fiche pour ce semestre.";
        this.showPopup = true;
        this.submissionSuccess = false;
        return;
      }
      if (new Date(this.fiche.dateDebut) > new Date(this.fiche.dateFin)) {
        this.popupMessage = "La date de début doit être antérieure à la date de fin.";
        this.showPopup = true;
        this.submissionSuccess = false;
        return;
      }
      if (!this.etudiant.idEtudiant) {
        this.popupMessage = "Impossible de créer la participation : étudiant non trouvé.";
        this.showPopup = true;
        this.submissionSuccess = false;
        return;
      }

      const participationPayload = {
        etudiant: { idEtudiant: this.etudiant.idEtudiant },
        action: { idAction: Number(this.fiche.action) },
        semestre: { idSemestre: Number(this.fiche.semestre) },
        idReferentiel: Number(this.fiche.referentiel),
        dateDebutParticipation: this.fiche.dateDebut,
        dateFinParticipation: this.fiche.dateFin,
        descriptionParticipation: this.fiche.description,
        totalPoints: null,
        nbParticipation: null,
        statut: null
      };

      fetch('http://localhost:8989/api/participes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(participationPayload)
      })
        .then(res => res.json())
        .then(() => {
          this.popupMessage = "Votre engagement fait la différence ! Participation créée avec succès.";
          this.showPopup = true;
          this.submissionSuccess = true;
        })
        .catch(() => {
          this.popupMessage = "Une erreur est survenue lors de l’envoi de la fiche.";
          this.showPopup = true;
          this.submissionSuccess = false;
        });
    },
    annuler() {
      this.fiche.semestre = '';
      this.fiche.referentiel = '';
      this.fiche.action = '';
      this.fiche.dateDebut = '';
      this.fiche.dateFin = '';
      this.fiche.description = '';
    },
    async downloadPDF() {
  const doc = new jsPDF({ orientation: 'portrait', unit: 'mm', format: 'a4' });

  const margin = 20;
  let y = margin;

  // ✅ Logo bien dimensionné
  const logo = await imageToBase64(logoUrl);
  const logoWidth = 35;
  const logoHeight = logoWidth * 1.48;
  doc.addImage(logo, 'PNG', margin, y, logoWidth, logoHeight);

  // ✅ Titre à droite du logo
  const titleX = margin + logoWidth + 10;
  const titleY = y + 10;

  doc.setFont('helvetica', 'bold');
  doc.setFontSize(17);
  doc.setTextColor(40, 40, 40);
  doc.text('FICHE DESCRIPTIVE', titleX, titleY);

  doc.setFont('helvetica', 'normal');
  doc.setFontSize(12);
  doc.setTextColor(106, 63, 160);
  doc.text('Ingénieur Engagé', titleX, titleY + 7);

  // Décalage après le logo
  y += logoHeight + 12;

  // 🔹 Ligne de séparation
  doc.setDrawColor(200);
  doc.line(margin, y, 190, y);

  // 🔹 Infos Étudiant
  y += 10;
  doc.setFont('helvetica', 'bold');
  doc.setFontSize(13);
  doc.setTextColor(0);
  doc.text('Informations Étudiant', margin, y);

  y += 6;
  doc.setDrawColor(230);
  doc.setFillColor(248, 248, 248);
  doc.roundedRect(margin, y - 3, 170, 16, 3, 3, 'FD');

  doc.setFont('helvetica', 'normal');
  doc.setFontSize(11);
  doc.setTextColor(50);
  doc.text(`Nom : ${this.etudiant.nom}`, margin + 4, y + 5);
  doc.text(`Prénom : ${this.etudiant.prenom}`, 120, y + 5);

  // 🔹 Bloc Participation
  y += 20;
  doc.setFont('helvetica', 'bold');
  doc.setFontSize(13);
  doc.setTextColor(0);
  doc.text('Participation', margin, y);

  y += 6;
  doc.setDrawColor(230);
  doc.setFillColor(248, 248, 248);
  doc.roundedRect(margin, y - 3, 170, 25, 3, 3, 'FD');

  doc.setFont('helvetica', 'normal');
  doc.setFontSize(11);
  doc.setTextColor(60);
  doc.text(`Semestre : S${this.fiche.semestre}`, margin + 4, y + 5);
  doc.text(`Référentiel : ${this.fiche.referentiel}`, 120, y + 5);
  doc.text(`Action : ${this.fiche.action}`, margin + 4, y + 12);
  doc.text(`Date début : ${this.fiche.dateDebut}`, 120, y + 12);
  doc.text(`Date fin : ${this.fiche.dateFin}`, 120, y + 19);

  // 🔹 Bloc Description
  y += 32;
  doc.setFont('helvetica', 'bold');
  doc.setFontSize(13);
  doc.setTextColor(0);
  doc.text('Description de la participation', margin, y);

  y += 6;
  const description = doc.splitTextToSize(this.fiche.description, 170);
  const descHeight = description.length * 6 + 8;
  doc.setDrawColor(210);
  doc.setFillColor(245, 245, 245);
  doc.roundedRect(margin, y - 3, 170, descHeight, 4, 4, 'FD');

  doc.setFont('helvetica', 'normal');
  doc.setFontSize(11);
  doc.setTextColor(50);
  doc.text(description, margin + 4, y + 5);

  // 🔹 Signatures
  y += descHeight + 20;
  doc.setDrawColor(220);
  doc.line(margin + 5, y, 80, y); // Étudiant
  doc.line(120, y, 185, y);      // Scolarité

  doc.setFontSize(10);
  doc.setTextColor(100);
  doc.text("Signature Étudiant", margin + 5, y + 5);
  doc.text("Visa Service Scolarité", 120, y + 5);

  // 🔹 Pied de page
  doc.setFontSize(9);
  doc.setTextColor(140);
  const today = new Date().toLocaleDateString('fr-FR');
  doc.text(`Document généré le ${today}`, margin, 290);

  // ✅ Génération
  doc.save(`Fiche_${this.etudiant.nom}_${this.etudiant.prenom}.pdf`);
  this.showPopup = false;
}



  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');

body {
  font-family: 'Poppins', sans-serif;
  background: #f4f4f9;
  margin: 0;
  padding: 0;
}

.fiche-container {
  max-width: 1200px;
  margin: 40px auto;
  padding: 40px;
  background: #ffffff;
  border-radius: 20px;
  box-shadow: 0 15px 40px rgba(217, 199, 199, 0.12);
  animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Header */
.header {
  background-size: cover;
  background-position: center;
  border-radius: 20px;
  padding: 60px 40px;
  margin-bottom: 40px;
  color: white;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #6A3FA0, #2980b9);
  z-index: 1;
}

.header-content {
  position: relative;
  z-index: 2;
}

.header h1 {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 10px;
  text-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
}

.header .description {
  font-size: 1.1rem;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  color: #ffffff;
}

/* Formulaire */
.fiche-form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 25px;
}

.required {
  color: red;
}

label {
  display: block;
  font-size: 1rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

input, textarea, select {
  width: 100%;
  padding: 14px;
  border: 1px solid #ddd;
  border-radius: 12px;
  font-size: 1rem;
  color: #333;
  background: #f9f9f9;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

input:focus, textarea:focus, select:focus {
  border-color: #6A3FA0;
  box-shadow: 0 0 12px rgba(106, 63, 160, 0.3);
  outline: none;
}

textarea {
  resize: vertical;
  min-height: 120px;
}

/* Message délai dépassé */
.depot-ferme-msg {
  background: #ffe0e0;
  color: #c0392b;
  padding: 12px;
  border-radius: 12px;
  font-size: 0.95rem;
  font-weight: 600;
  margin-top: 12px;
  display: inline-block;
  box-shadow: 0 0 0 1px #f3c0c0;
}

/* Boutons */
.fiche-form .form-actions,
.btn-group {
  display: flex;
  gap: 15px;
  margin-top: 30px;
}

button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 14px 28px;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-submit {
  background: linear-gradient(135deg, #6A3FA0, #2980b9);
  color: white;
}

.btn-cancel {
  background: linear-gradient(135deg, #ED6962, #c0392b);
  color: white;
}

.btn-submit:hover, .btn-cancel:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
}

.btn-submit:active, .btn-cancel:active {
  transform: translateY(0);
}

/* Popup */
.popup-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease-in-out;
}

.popup-content {
  background: white;
  padding: 30px;
  border-radius: 20px;
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
  max-width: 400px;
  width: 100%;
  text-align: center;
  animation: slideIn 0.3s ease-in-out;
  position: relative;
}

.popup-icon {
  margin-bottom: 20px;
}

.popup-icon svg {
  width: 60px;
  height: 60px;
  fill: #6A3FA0;
}

.popup-message {
  font-size: 1.2rem;
  color: #333;
  margin-bottom: 20px;
  line-height: 1.6;
}

.popup-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
}

.btn-close, .btn-download {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-close {
  background: linear-gradient(135deg, #ED6962, #c0392b);
  color: white;
}

.btn-download {
  background: linear-gradient(135deg, #7F56D9, #5F99AE);
  color: white;
}

.btn-close:hover, .btn-download:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
}

.btn-close:active, .btn-download:active {
  transform: translateY(0);
}

@keyframes slideIn {
  from { transform: translateY(-20px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

/* Responsive Design */
@media (max-width: 768px) {
  .header h1 {
    font-size: 2rem;
  }
  .header .description {
    font-size: 1rem;
  }
  .fiche-container {
    padding: 20px;
  }
  .form-actions, .btn-group {
    flex-direction: column;
  }
  button {
    width: 100%;
    margin-bottom: 10px;
  }
}
</style>
