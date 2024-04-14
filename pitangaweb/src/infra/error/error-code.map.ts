import { AxiosError } from "axios";
import errors from './error-messages.json';

export class ErrorCodeMap {
    private axiosMessages = new Map<string, string>();

    constructor() {
        Object.entries(errors).forEach(
            ([key, value]) => this.axiosMessages.set(key, value)
        );
    }

    public fromAxiosError(error: AxiosError): string {
        if(!this.axiosMessages.has(error.code!)) {
            return error.message;
        }
        return this.axiosMessages.get(error.code ?? 'default')!;
    }
}