"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaLocalizedText = void 0;
class UaLocalizedText {
    constructor(text, locale) {
        this._text = (text) ? text : "";
        this._locale = (locale) ? locale : null;
    }
    get text() {
        return this._text;
    }
    get locale() {
        return this._locale;
    }
    toString() {
        return `Text: ${this._text} Locale: ${this._locale}`;
    }
}
exports.UaLocalizedText = UaLocalizedText;
