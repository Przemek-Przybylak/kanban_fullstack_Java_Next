import { Loader2, AlertTriangle } from "lucide-react";
import React, { useEffect, useState } from "react";

type Props = {
  loading: boolean;
  error?: string | null;
  children: React.ReactNode;
};

export default function StatusWrapper({ loading, error, children }: Props) {
  const [visibleError, setVisibleError] = useState<string | null>(null);

  useEffect(() => {
    if (error) {
      setVisibleError(error);

      const timer = setTimeout(() => {
        setVisibleError(null);
      }, 5000); // 5 sekund

      return () => clearTimeout(timer);
    }
  }, [error]);

  return (
    <>
      {loading && (
        <div className="flex items-center justify-center h-[60vh] w-full">
          <div className="flex flex-col items-center gap-3 bg-white shadow-md rounded-xl px-6 py-5 border border-gray-200">
            <Loader2 className="h-8 w-8 text-blue-500 animate-spin" />
            <p className="text-gray-700 font-medium text-sm">
              Loading... (first load may take a while)
            </p>
          </div>
        </div>
      )}

      {children}

      {visibleError && (
        <div className="fixed top-4 left-1/2 -translate-x-1/2 z-50 bg-red-600 text-white px-6 py-3 rounded shadow-md">
          <div className="flex items-center gap-2">
            <AlertTriangle className="w-5 h-5" />
            <p className="text-sm">
              {visibleError === "FORBIDDEN" ? "Access denied" : visibleError}
            </p>
          </div>
        </div>
      )}
    </>
  );
}
