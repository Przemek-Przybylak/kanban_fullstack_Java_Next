import type { Metadata } from "next";
import "./globals.css";
import SideBar from "../components/SideBar/SideBar";
import React from "react";

const Inter = {
  fontFamily: "Inter, sans-serif",
};

export const metadata: Metadata = {
  title: "Kanban App",
  description: "Created by Przemysław Przybylak",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${Inter} h-screen bg-gray-50 text-gray-900 font-sans flex flex-col lg:flex-row overflow-hidden`}
      >
        <SideBar />
        <main className="flex-1 overflow-y-auto">{children}</main>
        <div className="fixed top-4 right-4 flex gap-4 z-50">
          <p className="cursor-pointer">login</p>
          <p className="cursor-pointer">register</p>
        </div>
      </body>
    </html>
  );
}
