export class Referentiel {
  constructor(idReferentiel, nom, description) {
    this._idReferentiel = idReferentiel;
    this._nom = nom;
    this._description = description;
  }

  get idReferentiel() { return this._idReferentiel; }
  set idReferentiel(value) { this._idReferentiel = value; }

  get nom() { return this._nom; }
  set nom(value) { this._nom = value; }

  get description() { return this._description; }
  set description(value) { this._description = value; }
}
