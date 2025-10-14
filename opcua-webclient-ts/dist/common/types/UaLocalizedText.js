"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.UaLocalizedText = void 0;
class UaLocalizedText {
    constructor(text, locale) {
        this._text = text;
        this._locale = locale;
    }
    get text() {
        return this._text;
    }
    get locale() {
        return this._locale;
    }
}
exports.UaLocalizedText = UaLocalizedText;
