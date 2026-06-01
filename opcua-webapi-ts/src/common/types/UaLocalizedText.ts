export class UaLocalizedText
{
    public static nullText: UaLocalizedText = new UaLocalizedText();

    private _text : string;
    private _locale : string | null;

    constructor(text?: string | null, locale? : string | null)
    {
        this._text = (text) ? text : "";
        this._locale = (locale) ? locale : null;
    }

    get text() : string
    {
        return this._text;
    }

    get locale() : string | null
    {
        return this._locale;
    }

    
    toString() : string
    {
        return `Text: ${this._text} Locale: ${this._locale}`;
    }

    static from(text: string) : UaLocalizedText
    {
        return new UaLocalizedText(text);
    }
}
