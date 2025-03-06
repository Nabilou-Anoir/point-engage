export class Semestre {
  constructor(idSemestre, nbSemestre, date) {
    this._idSemestre = idSemestre;
    this._nbSemestre = nbSemestre;
    this._date = date;
  }

  get idSemestre() { return this._idSemestre; }
  set idSemestre(value) { this._idSemestre = value; }

  get nbSemestre() { return this._nbSemestre; }
  set nbSemestre(value) { this._nbSemestre = value; }

  get date() { return this._date; }
  set date(value) { this._date = value; }
}
