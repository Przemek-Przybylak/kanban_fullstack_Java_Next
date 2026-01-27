"use client";

import React from "react";

import { useEffect, useState } from "react";
import { changeRole, getUsers } from "../../lib/api";

interface User {
  id: string;
  username: string;
  role: string;
}

export default function UsersTable() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadUsers = async () => {
    try {
      const data = await getUsers();
      if (data) setUsers(data);
    } catch (err) {
      setError("Cannot fetch users");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUsers();
  }, []);

  const handleRoleChange = async (id: string, newRole: string) => {
    try {
      await changeRole(id, newRole);
      setUsers((prev) =>
        prev.map((u) => (u.id === id ? { ...u, role: newRole } : u)),
      );
      alert("Role have been changed");
    } catch (err: any) {
      alert(err.message || "Error with Role change");
    }
  };

  if (loading) return <p className="p-4">Loading users...</p>;
  if (error) return <p className="p-4 text-red-500">{error}</p>;

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Users Management</h1>
      <div className="overflow-x-auto shadow-md rounded-lg">
        <table className="w-full text-left bg-white">
          <thead className="bg-gray-100 uppercase text-sm">
            <tr>
              <th className="p-4">ID</th>
              <th className="p-4">Username</th>
              <th className="p-4">Role</th>
              <th className="p-4">Action</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id} className="border-b hover:bg-gray-50">
                <td className="p-4 text-xs text-gray-500">{user.id}</td>
                <td className="p-4 font-medium">{user.username}</td>
                <td className="p-4">
                  <span
                    className={`px-2 py-1 rounded text-xs ${user.role === "ADMIN" ? "bg-purple-100 text-purple-700" : "bg-blue-100 text-blue-700"}`}
                  >
                    {user.role}
                  </span>
                </td>
                <td className="p-4">
                  <select
                    className="border rounded p-1 text-sm bg-transparent"
                    value={user.role}
                    onChange={(e) =>
                      handleRoleChange(user.id, e.target.value)
                    }
                  >
                    <option value="USER">Set USER</option>
                    <option value="ADMIN">Set ADMIN</option>
                  </select>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
