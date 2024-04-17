import './style.css';

type Props = {
  title: string;
  description: string;
  onClose: () => void;
  show: boolean;
};

export const DescriptionModal = (props: Props) => (
  <div className={props.show ? 'modal-container' : 'modal-container-hide'}>
    <div className='modal-bg'></div>
    <div className='modal'>
      <div className='grid grid-cols-6 border-b-2 p-1'>
        <h2 className='col-span-5 text-2xl font-bold'>{props.title}</h2>
        <button onClick={props.onClose} className='close-modal'>
          <img src='/pitanga-tcc/close.svg' alt='close' className='w-8 mx-auto h-8'/>
        </button>
      </div>
      <div>
        <p>{props.description}</p>
      </div>
    </div>
  </div>
);
