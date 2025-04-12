import React from 'react';
import { Link } from 'react-router-dom';

export default function Header() {
    return (
        <header>
            <nav>
                <ul>
                    <li><Link to="/dashboard">Dashboard</Link></li>
                    <li><Link to="/stations">Stations</Link></li>
                    <li><Link to="/passes">Passes</Link></li>
                    <li><Link to="/">Logout</Link></li>
                </ul>
            </nav>
        </header>
    );
}
