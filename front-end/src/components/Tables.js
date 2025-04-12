import React from 'react';

export default function Table({ columns, data }) {
    return (
        <table>
            <thead>
            <tr>
                {columns.map((col, index) => (
                    <th key={index}>{col}</th>
                ))}
            </tr>
            </thead>
            <tbody>
            {data.map((row, rowIndex) => (
                <tr key={rowIndex}>
                    {columns.map((col, colIndex) => (
                        <td key={colIndex}>{row[col]}</td>
                    ))}
                </tr>
            ))}
            </tbody>
        </table>
    );
}
