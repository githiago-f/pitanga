import { Challenge } from "../../../domain/problem";

type Props = {challenge: Challenge}

export const ChallengeItem = ({ challenge }: Props) => (
    <div className="border border-blue-300 shadow rounded-md p-4 max-w-sm w-full mx-auto my-2">
        <div className="w-full grid grid-cols-7">
            <div className="rounded-full bg-slate-700 h-10 w-10 col-span-1"></div>
            <div className="space-y-3 grid grid-cols-3 gap-4 col-span-6 pl-2">
                <h4 className="rounded text-ellipsis overflow-hidden col-span-2">
                    {challenge.title}
                </h4>
                <a className="rounded bg-lime-600 text-white p-1 text-center col-span-1" 
                    href={challenge.link}>Iniciar</a>
            </div>
        </div>
    </div>
);
