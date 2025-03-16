export const ELEVES = [
    {
      nom: "Ines Gribaa",
      promotion: "FIE4",
      typeEngagement: "École",
      resume: "Participation Abibus",
      valide: true,
      nbPointsEnvisages: 0.1,
      remarqueReferent: "",
      pointsAccordes: 0.1,
    },
    {
      nom: "Wilma Bourglia",
      promotion: "FIE4",
      typeEngagement: "Diversité",
      resume: "Tuteur Main à la pâte",
      valide: true,
      nbPointsEnvisages: 0.2,
      remarqueReferent: "",
      pointsAccordes: 0.2,
    },
    {
      nom: "Nabilou Anoir",
      promotion: "FIE4",
      typeEngagement: "Associatif",
      resume: "Trésorier BDE",
      valide: true,
      nbPointsEnvisages: 0.1,
      remarqueReferent: "un seul atelier réalisé",
      pointsAccordes: 0.1,
    },
  ];


export const validerEnvoi = (showModal) => {
    showModal.value = true;
};