import React from 'react';
import { Link } from 'react-router-dom';

export default function Sidebar() {
    return (
        <aside className="sidebar">
            <ul>
                <li><Link to="/dashboard">Dashboard</Link></li>
                <li><Link to="/stations">Stations</Link></li>
                <li><Link to="/passes">Passes</Link></li>
            </ul>
        </aside>
    );
}
