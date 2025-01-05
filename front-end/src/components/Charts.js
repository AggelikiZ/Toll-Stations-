import React from 'react';
import { Bar } from 'react-chartjs-2';
import { Chart as ChartJS, BarElement, CategoryScale, LinearScale, Tooltip } from 'chart.js';

ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip);

export default function Chart({ data }) {
    const chartData = {
        labels: ['Stations', 'Tags', 'Passes'],
        datasets: [
            {
                label: 'Counts',
                data: [data.nStations, data.nTags, data.nPasses],
                backgroundColor: ['#4caf50', '#2196f3', '#ff5722'],
            },
        ],
    };

    const options = {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
        },
    };

    return <Bar data={chartData} options={options} />;
}
