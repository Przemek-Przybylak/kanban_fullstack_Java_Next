import { Loader2, AlertTriangle } from "lucide-react";
import React from "react";

type Props = {
  loading: boolean;
  error?: string | null;
  children: React.ReactNode;
};

export default function StatusWrapper({ loading, error, children }: Props) {
  if (loading) {
    return (
      <div className="flex items-center justify-center h-[60vh] w-full">
        <div className="flex flex-col items-center gap-3 bg-white shadow-md rounded-xl px-6 py-5 border border-gray-200">
          <Loader2 className="h-8 w-8 text-blue-500 animate-spin" />
          <p className="text-gray-700 font-medium text-sm">
            Loading... ( first load may take a while )
          </p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center h-[60vh] w-full">
        <div className="flex flex-col items-center gap-2 bg-red-50 shadow-md rounded-xl px-6 py-5 border border-red-200 text-red-700">
          <AlertTriangle className="h-8 w-8" />
          <p className="font-semibold text-base">Ups... </p>
          <p className="text-sm text-center">
            {error}, please check connection or try again later
          </p>
        </div>
      </div>
    );
  }

  return <>{children}</>;
}
