type UploadedFile = {
  key: string;
  url?: string;
};

interface FileUploadHandler {
  upload(
    file: File,
    progressListener?: UploadProgressListener
  ): Promise<UploadedFile>;
  remove?(key: string): Promise<boolean>;
  buildUrl?(key: string, uploadedPrefix: string): string;
}

type Settings = {
  name: string;
  uploaded: {
    endpoint: string;
    prefix: string;
    sendRemoval: boolean;
  }
};

type Principal = {
  id: number;
  name: string;
};
