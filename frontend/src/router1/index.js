import { createRouter, createWebHistory } from "vue-router";

// Import de la vue de connexion
import LoginView from "@/views/LoginView.vue";

// Import des vues étudiant
import DashboardEtudiantView from "@/views/Etudiant/DashboardEtudiantView.vue";
import DispositifView from "@/views/Etudiant/DispositifView.vue";
import SaisirFicheView from "@/views/Etudiant/SaisirFicheView.vue";
import HistoriqueFichesView from "@/views/Etudiant/HistoriqueFichesView.vue";
import ProfilEtudiantView from "@/views/Etudiant/ProfilEtudiantView.vue";

// Import des autres vues (directeur, étudiant, etc.)
// import AccueilView from "@/views/DirecteurView/views/AccueilView.vue";
// import NotificationsView from "@/views/DirecteurView/views/NotificationsView.vue";
// import ProfilView from "@/views/DirecteurView/views/ProfilView.vue";
// import SessionsView from "@/views/DirecteurView/views/SessionsView.vue";
// import ReferentView from "@/views/DirecteurView/views/ReferentView.vue";
// import AttribuerPointsView from "@/views/DirecteurView/views/AttribuerPointsView.vue";
// import ModifierAcces from "@/views/DirecteurView/views/ModifierAcces.vue";
// import ModifierReferentielView from "@/views/DirecteurView/views/ModifierReferentielView.vue";
// import HistoriqueView from "@/views/DirecteurView/views/HistoriqueView.vue";
// import PointCommuleView from "@/views/DirecteurView/views/PointCommuleView.vue";


// import SaisirFicheView from "@/views/EtudiantView/SaisirFicheView.vue";
// import HistoriqueFichesView from "@/views/EtudiantView/HistoriqueFichesView.vue";
// import NotificationsEtudiantView from "@/views/EtudiantView/NotificationsEtudiantView.vue";
// import ProfilEtudiantView from "@/views/EtudiantView/ProfilEtudiantView.vue";

const routes = [
  // Redirige la route racine vers la page de connexion
  { path: "/", redirect: "/login" },

  // Route de la page de connexion
  { path: "/login", name: "Login", component: LoginView },

  // // Routes générales (exemple pour le directeur)
  // { path: "/accueil", name: "Accueil", component: AccueilView },
  // { path: "/notifications", name: "Notifications", component: NotificationsView },
  // { path: "/sessions", name: "Sessions", component: SessionsView },
  // { path: "/profil", name: "Profil", component: ProfilView },
  // { path: "/referent", name: "Referent", component: ReferentView },
  // { path: "/attribuer-points", name: "AttribuerPoints", component: AttribuerPointsView },
  // { path: "/modifier-acces", name: "ModifierAcces", component: ModifierAcces },
  // { path: "/modifier-referentiel", name: "ModifierReferentiel", component: ModifierReferentielView },
  // { path: "/historique", name: "Historique", component: HistoriqueView },
  // { path: "/points-cumules", name: "PointsCumules", component: PointCommuleView },


  // route etudiant
  {path: "/etudiant/accueil",name: "DashbordEtudiant", component: DashboardEtudiantView,},
  { path: "/dispositif", name: "Dispositif", component: DispositifView },
  { path: "/saisir-fiche", name: "SaisirFiche", component: SaisirFicheView},
  { path: "/historique-fiches", name: "HistoriqueFiches", component: HistoriqueFichesView},
  { path: "/etudiant/profil", name: "ProfilEtudiant", component: ProfilEtudiantView },
  //
  // { path: "/notifications-etudiant", name: "NotificationEtudiant", component: NotificationsEtudiantView },


];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
