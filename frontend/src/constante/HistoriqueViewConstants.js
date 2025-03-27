export const ELEVES = [
  {
    nom: "Ines Gribaa",
    promotion: "FIE4",
    typeEngagement: "École",
    resume: "Participation Abibus",
    ficheVisible: true, // true = œil ouvert, false = œil fermé
    valide: true,
    nbPointsEnvisages: 0.1,
    remarqueReferent: "",
    pointsAccordes: 0.1,
    envoye: false, // false = non envoyé, true = envoyé
  },
  {
    nom: "Wilma Bourglia",
    promotion: "FIE4",
    typeEngagement: "Diversité",
    resume: "Tuteur Main à la pâte",
    ficheVisible: false,
    valide: true,
    nbPointsEnvisages: 0.2,
    remarqueReferent: "",
    pointsAccordes: 0.2,
    envoye: true,
  },
  {
    nom: "Nabilou Anoir",
    promotion: "FIE4",
    typeEngagement: "Associatif",
    resume: "Trésorier BDE",
    ficheVisible: true,
    valide: true,
    nbPointsEnvisages: 0.1,
    remarqueReferent: "un seul atelier réalisé",
    pointsAccordes: 0.1,
    envoye: false,
  },
];


export const validerEnvoi = (showModal) => {
    showModal.value = true;
};