import { useMemo, useState } from 'react';
import { Link } from 'react-router-dom';

type Props = {
    titulo: string;
    onExecute: () => Promise<void>
}

export const ToolTray = (props: Props) => {
    const [isRunning, setIsRunning] = useState(false);

    const executeCode = () => {
        setIsRunning(true);
        props.onExecute().then(() => {
            setIsRunning(false);
        });
    };

    const animation = useMemo(() => (isRunning ? 'animate-spin' : ''), [isRunning]);

    return (
        <nav className="w-full p-2">
            <div className="grid grid-cols-6">
                <Link to={"/"} className="col-span-1 p-1 text-cyan-500 text-center">Voltar</Link>
                <div className="col-span-4 p-1 text-xl font-bold">
                    <h2>{props.titulo}</h2>
                </div>
                <div className="col-span-1">
                    <button 
                        className='bg-green-600 p-2 rounded w-full text-center'
                        onClick={executeCode}
                    >
                        <img 
                            src="gear-solid.svg" 
                            className={'h-5 w-full text-center ' + animation}/>
                    </button>
                </div>
            </div>
        </nav>
    );
}
