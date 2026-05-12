import React from 'react';
import { useParams } from 'react-router-dom';

const BookDetail = () => {
  const { id } = useParams();

  return (
    <div>
      <h1>Book Detail</h1>
      {/* TODO: Implement book detail page */}
    </div>
  );
};

export default BookDetail;
