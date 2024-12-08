import React from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const data = [
  {
    category: "Ordering System",
    implemented: 1,
    partial: 1,
    notImplemented: 1
  },
  {
    category: "Delivery System",
    implemented: 1,
    partial: 0,
    notImplemented: 0
  },
  {
    category: "Menu System",
    implemented: 1,
    partial: 0,
    notImplemented: 1
  },
  {
    category: "Driver System",
    implemented: 1,
    partial: 1,
    notImplemented: 0
  },
  {
    category: "Feedback System",
    implemented: 1,
    partial: 0,
    notImplemented: 0
  },
  {
    category: "Security",
    implemented: 0,
    partial: 0,
    notImplemented: 1
  },
  {
    category: "User System",
    implemented: 0,
    partial: 0,
    notImplemented: 1
  },
  {
    category: "System Core",
    implemented: 0,
    partial: 1,
    notImplemented: 0
  }
];

const FeatureStatus = () => {
  return (
    <div className="w-full h-96 p-4">
      <h2 className="text-xl font-bold mb-4">Feature Implementation Status by Category</h2>
      <ResponsiveContainer width="100%" height="100%">
        <BarChart
          data={data}
          layout="vertical"
          margin={{
            top: 20,
            right: 30,
            left: 100,
            bottom: 5
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis type="number" />
          <YAxis dataKey="category" type="category" />
          <Tooltip />
          <Legend />
          <Bar dataKey="implemented" name="Implemented" fill="#4CAF50" stackId="stack" />
          <Bar dataKey="partial" name="Partially Implemented" fill="#FFA726" stackId="stack" />
          <Bar dataKey="notImplemented" name="Not Implemented" fill="#EF5350" stackId="stack" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default FeatureStatus;